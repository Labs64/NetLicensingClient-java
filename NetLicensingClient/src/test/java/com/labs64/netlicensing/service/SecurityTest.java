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

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.SecurityMode;
import com.labs64.netlicensing.provider.RestProviderJersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for checking the ability to connect to services using different
 * security modes
 */
public class SecurityTest extends BaseServiceTest {

    // *** NLIC test mock resource ***

    @Path(REST_API_PATH)
    public static class NLICResource {

        @Path("get-auth-header")
        @GET
        public Response getAuthHeader(@HeaderParam("authorization") final String authorization) {
            return Response.ok(authorization).build();
        }

    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        return new ResourceConfig(NLICResource.class);
    }

    // *** NLIC Tests ***

    @Test
    public void testBasicAuthentication() throws Exception {
        final Context context = new Context()
                .setBaseUrl(BASE_URL)
                .setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                .setUsername("user1")
                .setPassword("pswrd");

        final String authHeader = RestProviderJersey.getInstance().call(context, HttpMethod.GET, "get-auth-header",
                null, String.class, null);

        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Basic "));

        final String[] userAndPassword = Base64.decodeAsString(authHeader.substring(6)).split(":");
        assertEquals("user1", userAndPassword[0]);
        assertEquals("pswrd", userAndPassword[1]);
    }

    @Test
    public void testApiKeyIdentification() throws Exception {
        final Context context = new Context()
                .setBaseUrl(BASE_URL)
                .setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION)
                .setApiKey("TEST_API_KEY");

        final String authHeader = RestProviderJersey.getInstance().call(context, HttpMethod.GET, "get-auth-header",
                null, String.class, null);
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Basic "));

        final String[] headerArray = Base64.decodeAsString(authHeader.substring(6)).split(":");
        assertEquals("apiKey", headerArray[0]);
        assertEquals("TEST_API_KEY", headerArray[1]);

    }
}
