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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.TokenImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Integration tests for {@link TokenService}.
 */
public class TokenServiceTest extends BaseServiceTest {

    // *** NLIC Tests ***

    private static Context context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final Token createdToken = TokenService.create(context, new TokenImpl());

        assertNotNull(createdToken);
        assertNotNull(createdToken.getNumber());
        assertEquals(true, createdToken.getActive());
        assertNotNull(createdToken.getExpirationTime());
        assertEquals(TokenType.DEFAULT, createdToken.getTokenType());
        assertEquals("VDEMO", createdToken.getVendorNumber());
    }

    @Test
    public void testCreateEmptyApiKeyToken() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.APIKEY);

        final Token createdToken = TokenService.create(context, newToken);

        assertNotNull(createdToken);
        assertNotNull(createdToken.getNumber());
        assertEquals(true, createdToken.getActive());
        assertNull(createdToken.getExpirationTime());
        assertEquals(TokenType.APIKEY, createdToken.getTokenType());
        assertEquals("VDEMO", createdToken.getVendorNumber());
    }

    @Test
    public void testCreateEmptyRegistrationToken() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.REGISTRATION);
        newToken.addProperty(Constants.Token.TOKEN_PROP_EMAIL, "test@test.com");

        final Token createdToken = TokenService.create(context, newToken);

        assertNotNull(createdToken);
        assertNotNull(createdToken.getNumber());
        assertEquals(true, createdToken.getActive());
        assertNotNull(createdToken.getExpirationTime());
        assertEquals(TokenType.REGISTRATION, createdToken.getTokenType());
        assertEquals("VDEMO", createdToken.getVendorNumber());
        assertEquals("test@test.com", createdToken.getTokenProperties().get(Constants.Token.TOKEN_PROP_EMAIL));
    }

    @Test
    public void testCreateEmptyPasswordResetToken() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.PASSWORDRESET);
        newToken.setVendorNumber("VDEMO2");
        newToken.addProperty(Constants.Token.TOKEN_PROP_EMAIL, "test@test.com");

        final Token createdToken = TokenService.create(context, newToken);

        assertNotNull(createdToken);
        assertNotNull(createdToken.getNumber());
        assertEquals(true, createdToken.getActive());
        assertNotNull(createdToken.getExpirationTime());
        assertEquals(TokenType.PASSWORDRESET, createdToken.getTokenType());
        assertEquals("VDEMO2", createdToken.getVendorNumber());
        assertEquals("test@test.com", createdToken.getTokenProperties().get(Constants.Token.TOKEN_PROP_EMAIL));
    }

    @Test
    public void testCreateEmptyShopToken() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.SHOP);
        newToken.addProperty(Constants.Licensee.LICENSEE_NUMBER, "L001-TEST");

        final Token createdToken = TokenService.create(context, newToken);

        assertNotNull(createdToken);
        assertNotNull(createdToken.getNumber());
        assertEquals(true, createdToken.getActive());
        assertNotNull(createdToken.getExpirationTime());
        assertEquals(TokenType.SHOP, createdToken.getTokenType());
        assertNotNull(createdToken.getTokenProperties().get(Constants.Token.TOKEN_PROP_SHOP_URL));
        assertEquals("L001-TEST", createdToken.getTokenProperties().get(Constants.Licensee.LICENSEE_NUMBER));
        assertEquals("VDEMO", createdToken.getVendorNumber());
    }

    @Test
    public void testCreateRegistrationTokenWithoutEmail() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.REGISTRATION);

        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Malformed token request, TokenValidation: Property 'email' not found");
        TokenService.create(context, newToken);
    }

    @Test
    public void testCreatePasswordResetTokenWithoutEmailAndVendorNumber() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.PASSWORDRESET);

        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Malformed token request, TokenValidation: Property 'email' not found, TokenValidation: Property 'vendorNumber' not found");
        TokenService.create(context, newToken);
    }

    @Test
    public void testCreateShopTokenWithoutLicenseeNumber() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.SHOP);

        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Malformed token request, TokenValidation: Property 'licenseeNumber' not found");
        TokenService.create(context, newToken);
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return TokenServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + TokenService.CONTEXT_PATH)
    public static class TokenServiceResource extends AbstractNLICServiceResource {

        public TokenServiceResource() {
            super("token");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            final String targetTokenType = formParams.getFirst(Constants.Token.TOKEN_TYPE);
            if (TokenType.REGISTRATION.name().equals(targetTokenType) && !formParams.containsKey(Constants.Token.TOKEN_PROP_EMAIL)) {
                return errorResponse("MalformedRequestException", "Malformed token request",
                        "TokenValidation", "Property 'email' not found");
            }
            if (TokenType.PASSWORDRESET.name().equals(targetTokenType) && !formParams.containsKey(Constants.Token.TOKEN_PROP_EMAIL) && !formParams.containsKey(Constants.Token.TOKEN_PROP_VENDORNUMBER)) {
                return errorResponse("MalformedRequestException", "Malformed token request",
                        "TokenValidation", "Property 'email' not found",
                        "TokenValidation", "Property 'vendorNumber' not found");
            }
            if (TokenType.SHOP.name().equals(targetTokenType) && !formParams.containsKey(Constants.Licensee.LICENSEE_NUMBER)) {
                return errorResponse("MalformedRequestException", "Malformed token request",
                        "TokenValidation", "Property 'licenseeNumber' not found");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.NUMBER, UUID.randomUUID().toString());
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            defaultPropertyValues.put(Constants.Token.TOKEN_TYPE, TokenType.DEFAULT.name());
            defaultPropertyValues.put(Constants.Token.TOKEN_PROP_VENDORNUMBER, "VDEMO");
            if (!TokenType.APIKEY.name().equals(targetTokenType)) {
                defaultPropertyValues.put(Constants.Token.EXPIRATION_TIME, DateUtils.printDate(DateUtils.getCurrentDate()));
            }
            if (TokenType.SHOP.name().equals(targetTokenType)) {
                defaultPropertyValues.put(Constants.Token.TOKEN_PROP_SHOP_URL, "https://netlicensing.labs64.com/app/v2/content/shop.xhtml?shoptoken=" +
                        defaultPropertyValues.get(Constants.NUMBER));
            }

            return create(formParams, defaultPropertyValues);
        }
    }

}
