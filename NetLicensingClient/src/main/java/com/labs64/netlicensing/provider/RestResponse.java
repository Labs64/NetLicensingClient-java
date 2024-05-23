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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import jakarta.ws.rs.core.MultivaluedHashMap;
//import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Contains info about response together with response entity.
 * 
 * @param <T>
 *            type of response entity
 */
public class RestResponse<T> {

    private int statusCode;
    
    private Map<String, List<String>> headers;

    private T entity;

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }
    
    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = new HashMap<>();
        headers.forEach((header, values) -> {
            List<String> this_values;
            if (!this.headers.containsKey(header)) {
                this_values = new ArrayList<>();
                this.headers.put(header, this_values);
            } else {
                this_values = this.headers.get(header);
            }
            values.forEach(value -> this_values.add(value));
        });
    }

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(final T entity) {
        this.entity = entity;
    }

}
