package com.labs64.netlicensing.util;

public class PackageUtils {

    private PackageUtils() {
        throw new IllegalStateException("PackageUtils class");
    }

    public static String getImplementationVersion() {
        //get implementation version
        return PackageUtils.class.getPackage().getImplementationVersion();
    }
}
