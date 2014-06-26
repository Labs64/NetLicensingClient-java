package com.labs64.netlicensing.provider;

import java.util.Map;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;

/**
 */
public interface RestProvider {

    /**
     * Helper method for performing REST requests with optional REST parameter map.
     * <p/>
     * This method has a long list of parameters. It is only intended for internal use.
     *
     * @param context      the context of service call
     * @param method       the HTTP method to be used, i.e. GET, PUT, POST.
     * @param urlTemplate  the REST URL urlTemplate.
     * @param request      optional: The request body to be sent to the server. May be null.
     * @param responseType optional: expected response type.
     *                     In case no responseType body is expected, responseType may be null.
     * @param namedParams  The URI parameters values which are expanded into the rest urlTemplate.
     * @param <REQ>        type of the request entity
     * @param <RES>        type of the responseType entity
     * @return the responseType entity received from the server, or null if responseType is null.
     */
    <REQ, RES> RES call(Context context, String method, String urlTemplate, REQ request, Class<RES> responseType, Map<String, Object> namedParams) throws RestException;

}
