package com.labs64.netlicensing.util;

public class SystemUtils {
    public static String getImplementationVersion() {
            //get implementation version
            return SystemUtils.class.getPackage().getImplementationVersion();
    }
}
