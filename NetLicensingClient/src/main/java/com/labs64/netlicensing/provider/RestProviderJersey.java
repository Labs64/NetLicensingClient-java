/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;

import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.provider.auth.Authentication;
import com.labs64.netlicensing.provider.auth.TokenAuthentication;
import com.labs64.netlicensing.provider.auth.UsernamePasswordAuthentication;

/**
 * Low level REST client implementation.
 * <p/>
 * This will also log each request in INFO level.
 */
public class RestProviderJersey extends AbstractRestProvider {

    private static final MediaType[] DEFAULT_ACCEPT_TYPES = { MediaType.APPLICATION_XML_TYPE };

    private static Client client;

    private final String basePath;

    /**
     * @param basePath
     *            base provider path
     */
    public RestProviderJersey(final String basePath) {
        this.basePath = basePath;
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

            final Entity<REQ> requestEntity = Entity.entity(request, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
            final Response response;
            if ((queryParams != null) && (queryParams.size() > 0)) {
                target = target.path(urlTemplate);
                for (final String paramKey : queryParams.keySet()) {
                    target = target.queryParam(paramKey, queryParams.get(paramKey));
                }
                response = target.request(DEFAULT_ACCEPT_TYPES)
                        .header(HttpHeaders.USER_AGENT, getUserAgent())
                        .method(httpMethod, requestEntity);
            } else {
                response = target.path(urlTemplate)
                        .request(DEFAULT_ACCEPT_TYPES)
                        .header(HttpHeaders.USER_AGENT, getUserAgent())
                        .method(httpMethod, requestEntity);
            }

            final RestResponse<RES> restResponse = new RestResponse<RES>();
            restResponse.setStatusCode(response.getStatus());
            restResponse.setEntity(readEntity(response, responseType));
            return restResponse;
        } catch (final RuntimeException e) {
            throw new RestException("Exception while calling service", e);
        }
    }

    private static String getUserAgent() {
        return "NetLicensing/Java " + System.getProperty("java.version") + " (http://netlicensing.labs64.com)";
    }

    /**
     * Get static instance of RESTful client
     * 
     * @return RESTful client
     */
    private static Client getClient() {
        // initialize client only once since it's expensive operation
        if (client == null) {
            synchronized (RestProviderJersey.class) {
                if (client == null) {
                    client = ClientBuilder.newClient(new ClientConfig());
                    client.register(new LoggingFilter());
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
        return getClient().target(basePath);
    }

    /**
     * @param target
     *            add headers to the object
     * @param auth
     *            authentication object to be added
     */
    private void addAuthHeaders(final WebTarget target, final Authentication auth) {
        if (auth != null) {
            if (auth instanceof UsernamePasswordAuthentication) {
                // see also https://jersey.java.net/documentation/latest/client.html#d0e4893
                final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
                        ((UsernamePasswordAuthentication) auth).getUsername(),
                        ((UsernamePasswordAuthentication) auth).getPassword());
                target.register(feature);
            } else if (auth instanceof TokenAuthentication) {
                final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("apiKey",
                        ((TokenAuthentication) auth).getToken());
                target.register(feature);
            }
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
            if (ex.getCause() instanceof NoContentException) {
                return null;
            } else {
                if ((response.getStatusInfo().getFamily() == Response.Status.Family.CLIENT_ERROR)
                        || (response.getStatusInfo().getFamily() == Response.Status.Family.SERVER_ERROR)) {
                    return null; // Ignore content interpretation errors if status is an error already
                }
                final String body = buffered ? " '" + response.readEntity(String.class) + "' of type '"
                        + response.getMediaType() + "'" : "";
                throw new RestException("Could not interpret the response body" + body, ex);
            }
        }
    }

}
