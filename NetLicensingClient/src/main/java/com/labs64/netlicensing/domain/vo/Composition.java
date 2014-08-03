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
package com.labs64.netlicensing.domain.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Composition implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Composition> properties;

    private String value;

    public Composition() { // list
        value = null;
    }

    public Composition(final String value) { // property value
        this.value = value;
    }

    public Map<String, Composition> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Composition>();
        }
        return properties;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void put(final String key, final String value) {
        getProperties().put(key, new Composition(value));
    }

    public void put(final String key, final Composition value) {
        getProperties().put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (value == null)  {
            sb.append("{");
            if (properties == null) {
                sb.append("<null>");
            } else {
                boolean first = true;
                for (Entry<String, Composition> prop : properties.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(prop.getKey()).append("=").append(prop.getValue());
                }
            }
            sb.append("}");
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

}
