package com.labs64.netlicensing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class TestHelpers {

    public static String loadFileContent(final String fileName) throws IOException {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream inputStream = classloader.getResourceAsStream(fileName);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
