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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Test;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.SecurityMode;
import com.labs64.netlicensing.schema.context.Info;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;

/**
 * Tests for checking the ability to connect to services using different
 * security modes
 */
public class SecurityTest extends BaseServiceTest {

    // *** NLIC Tests ***

    @Test
    public void testBasicAuthentication() throws Exception {
        final Context context = new Context()
                .setBaseUrl(BASE_URL)
                .setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                .setUsername("user1")
                .setPassword("pswrd");

        final Netlicensing res = NetLicensingService.request(context, HttpMethod.GET, "get-auth-header", null, null);
        final String authHeader = res.getInfos().getInfo().iterator().next().getValue();

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

        final Netlicensing res = NetLicensingService.request(context, HttpMethod.GET, "get-auth-header", null, null);
        final String authHeader = res.getInfos().getInfo().iterator().next().getValue();

        assertTrue(authHeader.startsWith("Basic "));

        final String[] headerArray = Base64.decodeAsString(authHeader.substring(6)).split(":");
        assertEquals("apiKey", headerArray[0]);
        assertEquals("TEST_API_KEY", headerArray[1]);

    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return NLICResource.class;
    };

    @Path(REST_API_PATH)
    public static class NLICResource {

        private final ObjectFactory objectFactory = new ObjectFactory();

        @Path("get-auth-header")
        @GET
        public Response getAuthHeader(@HeaderParam("authorization") final String authorization) {

            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            netlicensing.setInfos(objectFactory.createNetlicensingInfos());

            final Info info = objectFactory.createInfo();
            info.setValue(authorization);
            netlicensing.getInfos().getInfo().add(info);

            return Response.ok(netlicensing).build();
        }

    }
}
