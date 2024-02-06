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

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Contains info about response together with response entity.
 * 
 * @param <T>
 *            type of response entity
 */
public class RestResponse<T> {

    private int statusCode;
    
    private MultivaluedMap<String, Object> headers;

    private T entity;

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    public MultivaluedMap<String, Object> getHeaders() {
        return this.headers;
    }
    
    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = new MultivaluedHashMap<String, Object>();
        this.headers.putAll(headers);
    }

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(final T entity) {
        this.entity = entity;
    }

}
