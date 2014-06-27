package com.labs64.netlicensing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        public Response getAuthHeader(@HeaderParam("authorization") String authorization) {
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
