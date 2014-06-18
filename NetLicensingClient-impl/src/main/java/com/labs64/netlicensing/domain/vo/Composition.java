package com.labs64.netlicensing.domain.vo;

import java.util.HashMap;
import java.util.Map;

public class Composition {

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
            properties = new HashMap<>();
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

    @Override
    public String toString() {
        return "<Composition.toString() not implemented yet>";
    }

}
