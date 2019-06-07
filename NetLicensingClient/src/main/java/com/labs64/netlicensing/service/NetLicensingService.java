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
package com.labs64.netlicensing.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.EntityFactory;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.MetaInfo;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.provider.RestProvider;
import com.labs64.netlicensing.provider.RestProviderJersey;
import com.labs64.netlicensing.provider.RestResponse;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides generic requests to NetLicensing services. This class is supposed to be used by other **Service classes.
 */
public class NetLicensingService {

    private static NetLicensingService instance;

    private final EntityFactory entityFactory = new EntityFactory();

    /**
     * Private constructor
     */
    private NetLicensingService() {
    }

    /**
     * @return instance of NetLicensingService class
     */
    static NetLicensingService getInstance() {
        if (instance == null) {
            synchronized (NetLicensingService.class) {
                if (instance == null) {
                    instance = new NetLicensingService();
                }
            }
        }
        return instance;
    }

    /**
     * Helper method for performing GET request to NetLicensing API services. Finds and returns first suitable item with
     * type resultType from the response.
     *
     * @param context
     *            context for the NetLicensing API call
     * @param urlTemplate
     *            the REST URL template
     * @param queryParams
     *            The REST query parameters values. May be null if there are no parameters.
     * @param resultType
     *            the type of the result
     * @return first suitable item with type resultType from the response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    <RES> RES get(final Context context, final String urlTemplate, final Map<String, Object> queryParams,
            final Class<RES> resultType, final MetaInfo... meta) throws NetLicensingException {
        final Netlicensing netlicensing = request(context, HttpMethod.GET, urlTemplate, null, queryParams);
        if ((meta != null) && (meta.length > 0) && (meta[0] != null)) {
            if (netlicensing.getId() != null) {
                meta[0].setValue(Constants.PROP_ID, netlicensing.getId());
            }
        }
        return entityFactory.create(netlicensing, resultType);
    }

    /**
     * Helper method for performing GET request to NetLicensing API service that returns page of items with type
     * resultType.
     *
     * @param context
     *            context for the NetLicensing API call
     * @param urlTemplate
     *            the REST URL template
     * @param queryParams
     *            The REST query parameters values. May be null if there are no parameters.
     * @param resultType
     *            the type of the item of the result page
     * @return page of items with type resultType from the response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    <RES> Page<RES> list(final Context context, final String urlTemplate, final Map<String, Object> queryParams,
            final Class<RES> resultType) throws NetLicensingException {
        final Netlicensing netlicensing = request(context, HttpMethod.GET, urlTemplate, null, queryParams);
        return entityFactory.createPage(netlicensing, resultType);
    }

    /**
     * Helper method for performing POST request to NetLicensing API services. Finds and returns first suitable item
     * with type resultType from the response.
     *
     * @param context
     *            context for the NetLicensing API call
     * @param urlTemplate
     *            the REST URL template
     * @param request
     *            The request body to be sent to the server. May be null.
     * @param resultType
     *            the type of the result
     * @return first suitable item with type resultType from the response or null if response has no content
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    <RES> RES post(final Context context, final String urlTemplate, final Form request, final Class<RES> resultType,
            final MetaInfo... meta)
                    throws NetLicensingException {
        final Netlicensing netlicensing = request(context, HttpMethod.POST, urlTemplate, request, null);
        // if response has no content
        if (netlicensing == null) {
            return null;
        } else {
            if ((meta != null) && (meta.length > 0) && (meta[0] != null)) {
                if (netlicensing.getId() != null) {
                    meta[0].setValue(Constants.PROP_ID, netlicensing.getId());
                }
            }
            return entityFactory.create(netlicensing, resultType);
        }
    }

    /**
     * Helper method for performing DELETE request to NetLicensing API services.
     *
     * @param context
     *            context for the NetLicensing API call
     * @param urlTemplate
     *            the REST URL template
     * @param queryParams
     *            The REST query parameters values. May be null if there are no parameters.
     * @throws NetLicensingException
     */
    void delete(final Context context, final String urlTemplate, final Map<String, Object> queryParams)
            throws NetLicensingException {
        request(context, HttpMethod.DELETE, urlTemplate, null, queryParams);
    }

    /**
     * Helper method for performing request to NetLicensing API services. Knows about context for the NetLicensing API
     * calls, does authentication, provides error handling based on status of the response.
     *
     * @param context
     *            context for the NetLicensing API call
     * @param method
     *            the HTTP method to be used, i.e. GET, POST, DELETE
     * @param urlTemplate
     *            the REST URL template
     * @param request
     *            The request body to be sent to the server. May be null.
     * @param queryParams
     *            The REST query parameters values. May be null if there are no parameters.
     * @return {@link Netlicensing} response object
     * @throws NetLicensingException
     */
    public Netlicensing request(final Context context, final String method, final String urlTemplate,
            final Form request,
            final Map<String, Object> queryParams) throws NetLicensingException {
        CheckUtils.paramNotNull(context, "context");

        Form combinedRequest = request;
        Map<String, Object> combinedQueryParams = queryParams;
        if (StringUtils.isNotBlank(context.getVendorNumber())) {
            if (HttpMethod.POST.equals(method)) {
                if (combinedRequest == null) {
                    combinedRequest = new Form();
                }
                combinedRequest.param(Constants.Vendor.VENDOR_NUMBER, context.getVendorNumber());
            } else {
                if (combinedQueryParams == null) {
                    combinedQueryParams = new HashMap<>();
                }
                combinedQueryParams.put(Constants.Vendor.VENDOR_NUMBER, context.getVendorNumber());
            }
        }

        final RestProviderJersey restProvider = new RestProviderJersey(context.getBaseUrl());
        configure(restProvider, context);

        final RestResponse<Netlicensing> response = restProvider.call(method, urlTemplate, combinedRequest, Netlicensing.class,
                combinedQueryParams);

        final Response.Status status = Response.Status.fromStatusCode(response.getStatusCode());
        if (!isErrorStatus(status)) {
            switch (status) {
            case OK:
                return response.getEntity();
            case NO_CONTENT:
                return null;
            default:
                throw new RestException(String.format("Unsupported response status code %s: %s",
                        status.getStatusCode(), status.getReasonPhrase()));
            }
        } else {
            if (SchemaFunction.hasErrorInfos(response.getEntity())) {
                throw new ServiceException(status, response.getHeaders(), response.getEntity());
            } else {
                throw new RestException(String.format("Unknown service error %s: %s", status.getStatusCode(),
                        status.getReasonPhrase()));
            }
        }
    }

    /**
     * Passes the authentication data specified in the context of the call to the RESTful provider.
     *
     * @param restProvider
     *            RESTful provider to be authenticated
     * @param context
     *            additional context
     * @throws RestException
     */
    private void configure(final RestProvider restProvider, final Context context) throws RestException {
        if (context.getSecurityMode() == null) {
            throw new RestException("Security mode must be specified");
        }
        switch (context.getSecurityMode()) {
        case BASIC_AUTHENTICATION:
            restProvider.authenticate(context.getUsername(), context.getPassword());
            break;
        case APIKEY_IDENTIFICATION:
            restProvider.authenticate(context.getApiKey());
            break;
        case ANONYMOUS_IDENTIFICATION:
            break;
        default:
            throw new RestException("Unknown security mode");
        }
        if (context.containsKey(RestProvider.Configuration.class)) {
            final Object config = context.getObject(RestProvider.Configuration.class);
            if (config instanceof RestProvider.Configuration) {
                restProvider.configure((RestProvider.Configuration) config);
            }
        }
    }

    /**
     * @param status
     *            info about status
     * @return true if HTTP status represents client error or server error, false otherwise
     */
    private boolean isErrorStatus(final Response.Status status) {
        return (status.getFamily() == Response.Status.Family.CLIENT_ERROR)
                || (status.getFamily() == Response.Status.Family.SERVER_ERROR);
    }

}
