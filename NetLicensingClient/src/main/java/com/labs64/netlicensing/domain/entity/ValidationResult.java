package com.labs64.netlicensing.domain.entity;

import java.util.HashMap;
import java.util.Map;

import com.labs64.netlicensing.domain.vo.Composition;

public class ValidationResult {

    private Map<String, Composition> validations;

    public Map<String, Composition> getValidations() {
        if (validations == null) {
            validations = new HashMap<String, Composition>();
        }
        return validations;
    }

    public Composition getProductModuleValidation(final String productModuleNumber) {
        return getValidations().get(productModuleNumber);
    }

    public void setProductModuleValidation(final String productModuleNumber,
            final Composition productModuleValidation) {
        getValidations().put(productModuleNumber, productModuleValidation);
    }

    public void put(final String productModuleNumber, final String key, final String value) {
        getProductModuleValidation(productModuleNumber).put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ValidationResult [");
        boolean first = true;
        for (final Map.Entry<String, Composition> validationEntry : getValidations().entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("ProductModule<");
            builder.append(validationEntry.getKey());
            builder.append(">");
            builder.append(validationEntry.getValue().toString()); // TODO
        }
        builder.append("]");
        return builder.toString();
    }

}
