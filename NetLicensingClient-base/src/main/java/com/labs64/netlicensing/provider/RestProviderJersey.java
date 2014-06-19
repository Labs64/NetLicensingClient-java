package com.labs64.netlicensing.provider;


import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

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

    static final MediaType[] DEFAULT_ACCEPT_TYPES = {MediaType.APPLICATION_XML_TYPE};

    private Client client;

    public RestProviderJersey(final String basePath) {
        this.basePath = basePath;
        ClientConfig clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        client.register(new LoggingFilter());
    }

    @Override
    public <REQ, RES> RES call(final HttpMethod method, final String urlTemplate, final REQ request, final Class<RES> responseType, final Map<String, Object> namedParams) throws RestException {
        try {
            WebTarget target = getTarget();
            addAuthHeaders(authentication, target);

            Entity<REQ> requestEntity = Entity.entity(request, MediaType.APPLICATION_XML);
            if (namedParams == null) {
                return target.path(urlTemplate).request(DEFAULT_ACCEPT_TYPES).method(method.value(), requestEntity, responseType);
            } else {
                return target.path(urlTemplate).resolveTemplates(namedParams).request(DEFAULT_ACCEPT_TYPES).method(method.value(), requestEntity, responseType);
            }
        } catch (Throwable e) {
            throw new RestException("Service call error!", e);
        }
    }

    /**
     * Get the RESTful client target.
     *
     * @return RESTful client target
     */
    private WebTarget getTarget() {
        WebTarget target = client.target(basePath);
        return target;
    }

    private void addAuthHeaders(final Authentication auth, final WebTarget target) {
        if (auth != null) {
            if (auth instanceof UsernamePasswordAuthentication) {
                // see also https://jersey.java.net/documentation/latest/client.html#d0e4893
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(((UsernamePasswordAuthentication) auth).getUsername(), ((UsernamePasswordAuthentication) auth).getPassword());
                target.register(feature);
            } else if (auth instanceof TokenAuthentication) {
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("apiKey", ((TokenAuthentication) auth).getToken());
                target.register(feature);
            }
        }
    }

}
