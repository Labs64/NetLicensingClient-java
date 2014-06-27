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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides generic context.
 */
public class GenericContext<T> {

    private Map<String, T> contextMap = new ConcurrentHashMap<String, T>();

    public GenericContext() {
    }

    protected Map<String, T> getContextMap() {
        if (contextMap == null) {
            contextMap = new ConcurrentHashMap<String, T>();
        }
        return contextMap;
    }

    public boolean containsKey(final String key) {
        return contextMap.containsKey(key);
    }

    public GenericContext<T> setValue(final String key, final T value) {
        if (value != null) {
            getContextMap().put(key, value);
        }
        return this;
    }

    public GenericContext<T> setValue(final Class<?> key, final T value) {
        if (value == null) {
            return this;
        } else {
            return setValue(key.getName(), value);
        }
    }

    public GenericContext<T> setValue(final T value) {
        if (value == null) {
            return this;
        } else {
            return setValue(value.getClass(), value);
        }
    }

    public T getValue(final String key) {
        return contextMap.get(key);
    }

    public T getValue(final Class<?> key) {
        return contextMap.get(key.getName());
    }

}
