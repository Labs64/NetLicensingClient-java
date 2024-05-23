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
package com.labs64.netlicensing.exception;

import java.util.List;
import java.util.Map;

//import jakarta.ws.rs.core.MultivaluedHashMap;
//import jakarta.ws.rs.core.MultivaluedMap;
//import jakarta.ws.rs.core.Response;

import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Netlicensing;

/**
 * ServiceException is thrown when error response is received from the service.
 */
public class ServiceException extends NetLicensingException {

    private static final long serialVersionUID = 5253993578845477398L;

    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final Netlicensing errorResponse;

    /**
     * Construct a <code>ServiceException</code> with the service error response object.
     * 
     * @param status
     *            response status
     * @param headers
     *            response headers
     * @param errorResponse
     *            the service response containing the error info.
     */
    public ServiceException(final int statusCode, final Map<String, List<String>> headers, final Netlicensing errorResponse) {
        super(SchemaFunction.infosToMessage(errorResponse));
        this.statusCode = statusCode;
        this.headers = headers;
        this.errorResponse = errorResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Netlicensing getErrorResponse() {
        return errorResponse;
    }

}
