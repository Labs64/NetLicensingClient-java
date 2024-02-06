package com.labs64.netlicensing.util;

import java.util.Collection;
import java.util.Map.Entry;

import jakarta.ws.rs.core.Form;

import com.labs64.netlicensing.domain.entity.BaseEntity;

public class FormConverter {

    public static Form convert(BaseEntity entity) {
        final Form form = new Form();
        for (final Entry<String, Object> prop : entity.asMap().entrySet()) {
            if (prop.getValue() != null) {
                if (prop.getValue() instanceof Collection<?>) {
                    for (Object value: (Collection<?>) prop.getValue()) {
                        form.param(prop.getKey(), value.toString());
                    }
                } else {
                    form.param(prop.getKey(), prop.getValue().toString());
                }
            }
        }
        return form;
    }

}
