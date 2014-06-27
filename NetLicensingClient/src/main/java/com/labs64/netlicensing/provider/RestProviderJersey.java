package com.labs64.netlicensing.provider;


import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.provider.auth.Authentication;
import com.labs64.netlicensing.provider.auth.TokenAuthentication;
import com.labs64.netlicensing.provider.auth.UsernamePasswordAuthentication;

/**
 * Low level REST client implementation.
 * <p/>
 * This will also log each request in INFO level.
 */
public class RestProviderJersey implements RestProvider {

    private static final MediaType[] DEFAULT_ACCEPT_TYPES = {MediaType.APPLICATION_XML_TYPE};

    private static RestProviderJersey instance;

    private final Client client;

    /**
     * Private constructor
     */
    private RestProviderJersey() {
        client = ClientBuilder.newClient(new ClientConfig());
        client.register(new LoggingFilter());
    }

    public static RestProviderJersey getInstance() {
        if (instance == null) {
            synchronized (RestProviderJersey.class) {
                if (instance == null) {
                    instance = new RestProviderJersey();
                }
            }
        }
        return instance;
    }

    @Override
    public <REQ, RES> RES call(Context context, String httpMethod, String urlTemplate, REQ request, Class<RES> responseType, Map<String, Object> namedParams) throws RestException {
        try {
            final WebTarget target = getTarget(context.getBaseUrl());
            authenticate(context, target);

            final Entity<REQ> requestEntity = Entity.entity(request, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
            if (namedParams == null) {
                return target.path(urlTemplate).request(DEFAULT_ACCEPT_TYPES).method(httpMethod, requestEntity, responseType);
            } else {
                return target.path(urlTemplate).resolveTemplates(namedParams).request(DEFAULT_ACCEPT_TYPES).method(httpMethod, requestEntity, responseType);
            }
        } catch (Throwable e) {
            throw new RestException("Exception while calling service", e);
        }
    }

    /**
     * Get the RESTful client target
     *
     * @param basePath
     * @return RESTful client target
     */
    private WebTarget getTarget(String basePath) {
        WebTarget target = client.target(basePath);
        return target;
    }

    /**
     * @param context
     * @param target
     * @throws RestException
     */
    private void authenticate(Context context, final WebTarget target) throws RestException {
        Authentication auth;
        if (context.getSecurityMode() == null) {
            throw new RestException("Security mode must be specified");
        }
        switch (context.getSecurityMode()) {
            case BASIC_AUTHENTICATION:
                auth = new UsernamePasswordAuthentication(context.getUsername(), context.getPassword());
                break;
            case APIKEY_IDENTIFICATION:
                auth = new TokenAuthentication(context.getApiKey());
                break;
            default:
                throw new RestException("Unknown security mode");
        }
        addAuthHeaders(auth, target);
    }

    /**
     * @param auth
     * @param target
     */
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
