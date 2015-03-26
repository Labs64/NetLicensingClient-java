package com.labs64.netlicensing.domain.vo;

import java.util.HashMap;
import java.util.Map;

public class ValidationParameters {

    private Map<String, Map<String, String>> parameters;

    public Map<String, Map<String, String>> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<String, Map<String, String>>();
        }
        return parameters;
    }

    public Map<String, String> getProductModuleValidationParameters(final String productModuleNumber) {
        if (!getParameters().containsKey(productModuleNumber)) {
            getParameters().put(productModuleNumber, new HashMap<String, String>());
        }
        return getParameters().get(productModuleNumber);
    }

    public void setProductModuleValidationParameters(final String productModuleNumber,
            final Map<String, String> productModuleParameters) {
        getParameters().put(productModuleNumber, productModuleParameters);
    }

    public void put(final String productModuleNumber, final String key, final String value) {
        getProductModuleValidationParameters(productModuleNumber).put(key, value);
    }

}
