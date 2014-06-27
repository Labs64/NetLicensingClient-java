package com.labs64.netlicensing.service;

import org.glassfish.jersey.test.JerseyTest;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.SecurityMode;

/**
 */
abstract class BaseServiceTest extends JerseyTest {

    static final String REST_API_PATH = "/core/v2/rest";
    static final String BASE_URL_UNITTEST = "http://localhost:9998";
    static final String BASE_URL_PROD = "https://netlicensing.labs64.com";

    static final String BASE_URL = BASE_URL_UNITTEST + REST_API_PATH;

    static final String USER = "demo";
    static final String PASS = "demo";

    static final String TEST_CASE_BASE = "mock/";

    static Context createContext() {
        return new Context()
                .setBaseUrl(BASE_URL)
                .setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                .setUsername(USER)
                .setPassword(PASS);
    }

}
