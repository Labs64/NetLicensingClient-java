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
package com.labs64.netlicensing.domain.vo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides generic context.
 */
public class GenericContext<T> {

    private Map<String, Object> contextMap = new ConcurrentHashMap<>();

    private final Class<T> valueClass;

    public GenericContext(final Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    protected Map<String, Object> getContextMap() {
        if (contextMap == null) {
            contextMap = new ConcurrentHashMap<>();
        }
        return contextMap;
    }

    public boolean containsKey(final String key) {
        return getContextMap().containsKey(key);
    }

    public boolean containsKey(final Class<?> key) {
        return getContextMap().containsKey(key.getName());
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
        final Object value = getContextMap().get(key);
        if ((value != null) && valueClass.isAssignableFrom(value.getClass())) {
            return valueClass.cast(value);
        } else {
            return null;
        }
    }

    public T getValue(final Class<?> key) {
        return getValue(key.getName());
    }

    public GenericContext<T> setObject(final String key, final Object value) {
        if (value != null) {
            getContextMap().put(key, value);
        }
        return this;
    }

    public GenericContext<T> setObject(final Class<?> key, final Object value) {
        if (value == null) {
            return this;
        } else {
            return setObject(key.getName(), value);
        }
    }

    public GenericContext<T> setObject(final Object value) {
        if (value == null) {
            return this;
        } else {
            return setObject(value.getClass(), value);
        }
    }

    public Object getObject(final String key) {
        return getContextMap().get(key);
    }

    public Object getObject(final Class<?> key) {
        return getObject(key.getName());
    }
}
