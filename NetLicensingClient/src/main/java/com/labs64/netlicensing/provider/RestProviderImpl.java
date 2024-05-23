/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.JAXBException;

import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.provider.auth.Authentication;
import com.labs64.netlicensing.util.ConvertUtils;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Low level REST client implementation.
 * <p>
 * This will also log each request in INFO level.
 */
public class RestProviderImpl extends AbstractRestProvider {

    private final String basePath;

    private class DefaultConfig implements RestProvider.Configuration {

        @Override
        public String getUserAgent() {
            return "NetLicensing/Java " + System.getProperty("java.version") + " (https://netlicensing.io)";
        }

        @Override
        public boolean isLoggingEnabled() {
            return true;
        }

    }

    /**
     * @param basePath
     *            base provider path
     */
    public RestProviderImpl(final String basePath) {
        this.basePath = basePath;
        configure(new DefaultConfig());
    }

    /*
     * @see com.labs64.netlicensing.provider.RestProvider#call(java.lang.String, java.lang.String, java.lang.Object,
     * java.lang.Class, java.util.Map)
     */
    @Override
    public <REQ, RES> RestResponse<RES> call(final String httpMethod, final String urlTemplate, final REQ request_arg,
            final Class<RES> responseType, final Map<String, String> queryParams) throws RestException {
        try {
            // Not using authenticator to ensure preemptive authentication
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .followRedirects(Redirect.NORMAL)
                    .build();

            String query;
            if ((queryParams == null) || (queryParams.isEmpty())) {
                query = "";
            } else {
                Map<String, String> castedQueryParams = new HashMap<>();
                queryParams.forEach((key, val) -> {
                    castedQueryParams.put(key, val.toString());
                });
                query = "?" + ConvertUtils.mapToParamString(castedQueryParams);
            }
            Authentication auth = getAuthentication();
            String valueToEncode = auth.getUsername() + ":" + auth.getPassword();
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());

            Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(this.basePath + "/" + urlTemplate + query))
                    .header("Authorization", authHeader)  // Preemptive
                    .header("User-Agent", getConfiguration().getUserAgent());

            if (httpMethod == "POST" || httpMethod == "PUT") {
                String postParams = ConvertUtils.mapToParamString(((Form)request_arg).getParams());
                requestBuilder
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(httpMethod, BodyPublishers.ofString(postParams));
            } else {
                requestBuilder.method(httpMethod, BodyPublishers.noBody());
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            final RestResponse<RES> restResponse = new RestResponse<>();
            restResponse.setStatusCode(response.statusCode());
            restResponse.setHeaders(response.headers().map());
            if ((response.statusCode() != 204) || !response.body().isEmpty()) {
                restResponse.setEntity(readEntity(response.body(), response.statusCode(), responseType));
            }
            return restResponse;
        } catch (final IOException | InterruptedException e) {
            throw new RestException("Exception while calling service.", e);
        }
    }

    /**
     * Reads entity of given type from response. Returns null when the response has a zero-length content.
     *
     * @param response
     *            service response
     * @param responseType
     *            expected response type
     * @return the response entity
     * @throws RestException
     */
    private <RES> RES readEntity(final String body, int statusCode, final Class<RES> responseType) throws RestException {
        if ((statusCode == 204) && body.isEmpty()) {
            return null;
        }

        try {
            return JAXBUtils.readObjectFromString(body, responseType);
        } catch (final JAXBException ex) {
            int statusFamily = statusCode / 100;
            if ((statusFamily == 4) || (statusFamily == 5)) {  // 4xx - Client Error, 5xx - Server Error
                return null; // Ignore content interpretation errors if status is an error already
            }
            throw new RestException("Could not interpret the response body.\n" + body, ex);
        }
    }

}
