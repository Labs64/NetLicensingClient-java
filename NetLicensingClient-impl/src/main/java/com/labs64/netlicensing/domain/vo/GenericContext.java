package com.labs64.netlicensing.domain.vo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides generic context.
 */
public class GenericContext<T> {

    private Map<String, T> contextMap = new ConcurrentHashMap<>();

    public GenericContext() {
    }

    protected Map<String, T> getContextMap() {
        if (contextMap == null) {
            contextMap = new ConcurrentHashMap<>();
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
