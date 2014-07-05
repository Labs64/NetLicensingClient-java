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

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.provider.RestProvider;
import com.labs64.netlicensing.provider.RestProviderJersey;
import com.labs64.netlicensing.provider.RestResponse;
import com.labs64.netlicensing.schema.context.Info;
import com.labs64.netlicensing.schema.context.Netlicensing;

/**
 * Provides generic request to NetLicensing services. This class is supposed to
 * be used by other **Service classes.
 */
class NetLicensingService {

    /**
     * Helper method for performing request to NetLicensing API services. Knows
     * about context for the NetLicensing API calls, does authentication,
     * provides error handling based on status of the response.
     *
     * @param context
     *            context for the NetLicensing API calls
     * @param method
     *            the HTTP method to be used, i.e. GET, PUT, POST.
     * @param urlTemplate
     * @param request
     *            The request body to be sent to the server. May be null.
     * @param namedParams
     *            The URI parameters values which are expanded into the rest
     *            urlTemplate. May be null if there are no parameters.
     * @param <REQ>
     *            type of the request entity
     * @return
     * @throws RestException
     */
    static <REQ> Netlicensing request(final Context context, final String method, final String urlTemplate,
            final REQ request, final Map<String, Object> namedParams) throws RestException {

        final RestProviderJersey restProvider = new RestProviderJersey(context.getBaseUrl());
        authenticate(restProvider, context);

        final RestResponse<Netlicensing> response = restProvider.call(method, urlTemplate, request, Netlicensing.class,
                namedParams);

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
            if (hasErrorInfos(response.getEntity())) {
                final List<Info> infos = response.getEntity().getInfos().getInfo();
                throw new RestException(asExceptionMessage(infos));
            } else {
                throw new RestException(String.format("Service error %s: %s", status.getStatusCode(),
                        status.getReasonPhrase()));
            }
        }
    }

    /**
     * Passes the authentication data specified in the context of the call to
     * the RESTful provider.
     *
     * @param restProvider
     * @param context
     * @throws RestException
     */
    private static void authenticate(final RestProvider restProvider, final Context context) throws RestException {
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
            default:
                throw new RestException("Unknown security mode");
        }
    }

    /**
     * @param status
     *            info about status
     * @return true if HTTP status represents client error or server error,
     *         false otherwise
     */
    private static boolean isErrorStatus(final Response.Status status) {
        return status.getFamily() == Response.Status.Family.CLIENT_ERROR
                || status.getFamily() == Response.Status.Family.SERVER_ERROR;
    }

    /**
     * @param entity
     * @return
     */
    private static boolean hasErrorInfos(final Netlicensing entity) {
        return entity != null && entity.getInfos() != null && !entity.getInfos().getInfo().isEmpty();
    }

    /**
     * @param infos
     * @return
     */
    private static String asExceptionMessage(final List<Info> infos) {
        final StringBuilder errorMessages = new StringBuilder();
        for (Info info : infos) {
            if (errorMessages.length() > 0) {
                errorMessages.append(", ");
            }
            errorMessages.append(info.getId()).append(": ").append(info.getValue());
        }
        return errorMessages.toString();
    }

}
