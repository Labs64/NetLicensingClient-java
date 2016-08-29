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
package com.labs64.netlicensing.domain.entity;

import java.io.Serializable;
import java.util.Map;

import javax.ws.rs.core.Form;

/**
 * Defines properties common to all (or most) of other entities.
 */
public interface BaseEntity extends Serializable {

    // Methods for working with properties

    String getNumber();

    void setNumber(String number);

    Boolean getActive();

    void setActive(Boolean active);

    // Methods for working with custom properties

    Map<String, String> getProperties();

    void addProperty(String property, String value);

    void removeProperty(final String property);

    /**
     * Converts properties of the entity to the body of POST request
     *
     * @return object that represents HTML form data request encoded using the "application/x-www-form-urlencoded"
     *         content type
     */
    Form asRequestForm();

}
