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
package com.labs64.netlicensing.domain.entity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.BaseEntity;
import com.labs64.netlicensing.util.Visitable;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.BaseEntity}.
 */
public abstract class BaseEntityImpl extends Visitable implements BaseEntity {

    private static final long serialVersionUID = -3912283193861706866L;

    private String number;

    private Boolean active;

    private Map<String, String> properties;

    /**
     * List of reserved properties is used for handling of custom properties. Property name that is included in the list
     * can not be used as custom property name. The list is extended by each derived entity class until the final
     * business entity.
     *
     * @return the list of reserved property names
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = new ArrayList<>();
        reserved.add(Constants.ID);
        reserved.add(Constants.NUMBER);
        reserved.add(Constants.ACTIVE);
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
    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getProperties().remove(property);
    }

    @Override
    public String toString() {
        return toString(asMap());
    }
    
    @Override
    public Map<String, Object> asMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put(Constants.NUMBER, getNumber());
        map.put(Constants.ACTIVE, getActive());
        if (properties != null) {
            for (final Map.Entry<String, String> lp : properties.entrySet()) {
                map.put(lp.getKey(), lp.getValue());
            }
        }
        return map;
    }

    protected String toString(final Map<String, Object> propMap) {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [");
        boolean firstProp = true;
        for (final String propKey : propMap.keySet()) {
            final Object propValue = propMap.get(propKey);
            if (propValue != null) {
                builder.append(firstProp ? "" : ", ");
                firstProp = false;

                builder.append(propKey).append("=");
                final String propValueStr = String.valueOf(propValue);
                builder.append(propValueStr.length() > 50 ? propValueStr.substring(0, 50) : propValue);
            }
        }
        return builder.append("]").toString();
    }

}
