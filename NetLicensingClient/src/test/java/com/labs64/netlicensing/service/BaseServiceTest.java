/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
