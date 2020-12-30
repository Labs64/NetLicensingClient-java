package com.labs64.netlicensing.demo;

import java.util.HashMap;
import java.util.Map;

import com.labs64.netlicensing.examples.CallEveryAPIMethod;
import com.labs64.netlicensing.examples.NetLicensingExample;
import com.labs64.netlicensing.examples.OfflineValidation;

public class AllExamples {

    public static Map<String, Class<? extends NetLicensingExample>> list = new HashMap<>();

    private static void addExample(final Class<? extends NetLicensingExample> exampleClass) {
        list.put(exampleClass.getSimpleName(), exampleClass);
    }

    static
    {
        addExample(CallEveryAPIMethod.class);
        addExample(OfflineValidation.class);
    }
}
