package com.labs64.netlicensing.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.labs64.netlicensing.domain.entity.BaseEntity;
import com.labs64.netlicensing.provider.Form;

public class ConvertUtils {

    public static Form entityToForm(BaseEntity entity) {
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
    
    public static String mapToParamString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (paramStr.length() != 0)
                paramStr.append('&');
            paramStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            paramStr.append('=');
            paramStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return paramStr.toString();
    }
    
}
