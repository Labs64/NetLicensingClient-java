package com.labs64.netlicensing.examples;

import java.util.HashMap;
import java.util.Map;

public class AllExamples {
    
    public static Map<String, Class<? extends NetLicensingExample>> list = new HashMap<>();

    private static void addExample(Class<? extends NetLicensingExample> exampleClass) {
        list.put(exampleClass.getSimpleName(), exampleClass);
    }

    static
    {
        addExample(CallEveryAPIMethod.class);
    }
}
