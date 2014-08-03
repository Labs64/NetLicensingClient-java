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
package com.labs64.netlicensing.domain.entity.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Form;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.BaseEntity;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.BaseEntity}.
 */
public abstract class BaseEntityImpl implements BaseEntity {

    private static final long serialVersionUID = -3912283193861706866L;

    private String number;

    private Boolean active;

    /**
     * List of reserved properties is used for handling of custom properties. Property name that is included in the list
     * can not be used as custom property name. The list is extended by each derived entity class until the final
     * business entity.
     *
     * @return the list of reserved property names
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = new ArrayList<String>();
        reserved.add(Constants.ID);
        reserved.add(Constants.NUMBER);
        reserved.add(Constants.ACTIVE);
        reserved.add(Constants.DELETED);
        return reserved;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(final String number) {
        this.number = number;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public void setActive(final Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return toString(asPropertiesMap());
    }

    @Override
    public Form asRequestForm() {
        final Form form = new Form();
        final Map<String, Object> propMap = asPropertiesMap();
        for (final String propKey : propMap.keySet()) {
            final Object propValue = propMap.get(propKey);
            if (propValue != null) {
                form.param(propKey, propValue.toString());
            }
        }
        return form;
    }

    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(Constants.NUMBER, getNumber());
        map.put(Constants.ACTIVE, getActive());
        return map;
    }

    protected String toString(final Map<String, Object> propMap) {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [");
        boolean firstProp = true;
        for (final String propKey : propMap.keySet()) {
            builder.append(firstProp ? "" : ", ");
            firstProp = false;

            final String propValue = String.valueOf(propMap.get(propKey));
            builder.append(propKey).append("=")
                    .append(propValue.length() > 50 ? propValue.substring(0, 50) : propValue);
        }
        return builder.append("]").toString();
    }

}
