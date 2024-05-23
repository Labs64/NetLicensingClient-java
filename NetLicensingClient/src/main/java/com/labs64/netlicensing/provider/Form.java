package com.labs64.netlicensing.provider;

import java.util.HashMap;
import java.util.Map;

public class Form {
    
    private Map<String, String> params;

    public Form() {
        params = new HashMap<>();
    }
    
    public void param(String key, String value) {
        params.put(key, value);
    }
    
    public Map<String, String> getParams() {
        return params;
    }
    
}
