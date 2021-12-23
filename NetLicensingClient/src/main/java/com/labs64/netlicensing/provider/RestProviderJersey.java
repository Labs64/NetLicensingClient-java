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

import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.provider.auth.Authentication;

/**
 * Low level REST client implementation.
 * <p>
 * This will also log each request in INFO level.
 */
public class RestProviderJersey extends AbstractRestProvider {

    private static final MediaType[] DEFAULT_ACCEPT_TYPES = { MediaType.APPLICATION_XML_TYPE };

    private static Client client;

    private final String basePath;

    private class JerseyDefaultConfig implements RestProvider.Configuration {

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
    public RestProviderJersey(final String basePath) {
        this.basePath = basePath;
        configure(new JerseyDefaultConfig());
    }

    /*
     * @see com.labs64.netlicensing.provider.RestProvider#call(java.lang.String, java.lang.String, java.lang.Object,
     * java.lang.Class, java.util.Map)
     */
    @Override
    public <REQ, RES> RestResponse<RES> call(final String httpMethod, final String urlTemplate, final REQ request,
            final Class<RES> responseType,
            final Map<String, Object> queryParams) throws RestException {
        try {
            WebTarget target = getTarget(this.basePath);
            addAuthHeaders(target, getAuthentication());
            target = target.path(urlTemplate);
            if ((queryParams != null) && (queryParams.size() > 0)) {
                for (final String paramKey : queryParams.keySet()) {
                    target = target.queryParam(paramKey, queryParams.get(paramKey));
                }
            }

            final Response response;
            final Builder builder = target.request(DEFAULT_ACCEPT_TYPES).header(HttpHeaders.USER_AGENT,
                    getConfiguration().getUserAgent());
            if ("POST".equals(httpMethod) || "PUT".equals(httpMethod)) {
                final Entity<REQ> requestEntity = Entity.entity(request, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
                response = builder.method(httpMethod, requestEntity);
            } else {
                response = builder.method(httpMethod);
            }

            final RestResponse<RES> restResponse = new RestResponse<>();
            restResponse.setStatusCode(response.getStatus());
            restResponse.setHeaders(response.getHeaders());
            restResponse.setEntity(readEntity(response, responseType));
            return restResponse;
        } catch (final RuntimeException e) {
            throw new RestException("Exception while calling service.", e);
        }
    }

    /**
     * Get static instance of RESTful client
     *
     * @return RESTful client
     */
    private static Client getClient(final RestProvider.Configuration configuration) {
        // initialize client only once since it's expensive operation
        if (client == null) {
            synchronized (RestProviderJersey.class) {
                if (client == null) {
                    client = ClientBuilder.newClient(new ClientConfig());
                    if (configuration.isLoggingEnabled()) {
                        //TODO: enable logging for jersey 2.26
                        //client.register(new LoggingFilter());
                    }
                }
            }
        }
        return client;
    }

    /**
     * Get the RESTful client target
     *
     * @param basePath
     *            base provider path
     * @return RESTful client target
     */
    private WebTarget getTarget(final String basePath) {
        return getClient(getConfiguration()).target(basePath);
    }

    /**
     * @param target
     *            target object, to which the authentication headers will be added
     * @param auth
     *            an object providing the authentication info
     */
    private void addAuthHeaders(final WebTarget target, final Authentication auth) {
        if (auth != null) {
            // see also https://jersey.java.net/documentation/latest/client.html, chapter "Securing a Client"
            target.register(HttpAuthenticationFeature.basic(auth.getUsername(), auth.getPassword()));
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
    private <RES> RES readEntity(final Response response, final Class<RES> responseType) throws RestException {
        boolean buffered = false;
        try {
            buffered = response.bufferEntity();
            return response.readEntity(responseType);
        } catch (final ProcessingException ex) {
            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || ex.getCause() instanceof NoContentException) {
                return null;
            } else {
                if ((response.getStatusInfo().getFamily() == Response.Status.Family.CLIENT_ERROR)
                        || (response.getStatusInfo().getFamily() == Response.Status.Family.SERVER_ERROR)) {
                    return null; // Ignore content interpretation errors if status is an error already
                }
                final String body = buffered ? " '" + response.readEntity(String.class) + "' of type '"
                        + response.getMediaType() + "'" : "";
                throw new RestException("Could not interpret the response body" + body + ".", ex);
            }
        }
    }

}
