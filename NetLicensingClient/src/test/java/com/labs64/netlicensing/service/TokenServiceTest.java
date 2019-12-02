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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.impl.TokenImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.util.DateUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertNotNull(createdToken.getProperties().get(Constants.Token.TOKEN_PROP_SHOP_URL));
        assertEquals("L001-TEST", createdToken.getProperties().get(Constants.Licensee.LICENSEE_NUMBER));
        assertEquals("VDEMO", createdToken.getVendorNumber());
    }

    @Test
    public void testCreateShopTokenWithoutLicenseeNumber() throws Exception {
        final TokenImpl newToken = new TokenImpl();
        newToken.setTokenType(TokenType.SHOP);

        thrown.expect(ServiceException.class);
        thrown.expectMessage("MalformedRequestException: Malformed token request, TokenValidation: Property 'licenseeNumber' not found");
        TokenService.create(context, newToken);
    }

    @Test
    public void testGet() throws Exception {
        final Token token = TokenService.get(context, "afeb41d9-314e-49be-8465-148c614badfa");

        assertNotNull(token);
        assertEquals("afeb41d9-314e-49be-8465-148c614badfa", token.getNumber());
        assertEquals(true, token.getActive());
        assertEquals(DateUtils.parseDate("2014-07-23T15:19:56.147Z").getTime(), token.getExpirationTime());
        assertEquals(TokenType.SHOP, token.getTokenType());
        assertEquals(
                "https://go.netlicensing.io/shop/v2/?shoptoken=afeb41d9-314e-49be-8465-148c614badfa",
                token.getProperties().get(Constants.Token.TOKEN_PROP_SHOP_URL));
        assertEquals("L001-TEST", token.getProperties().get(Constants.Licensee.LICENSEE_NUMBER));
        assertEquals("VDEMO", token.getVendorNumber());
    }

    @Test
    public void testList() throws Exception {
        final Page<Token> tokens = TokenService.list(context, null);

        assertNotNull(tokens);
        assertTrue(tokens.hasContent());
        assertEquals(3, tokens.getItemsNumber());
        assertEquals("08b66094-a5c4-4c93-be71-567e982d9428", tokens.getContent().get(0).getNumber());
        assertEquals(DateUtils.parseDate("2014-07-22T23:07:46.742Z").getTime(), tokens.getContent().get(1)
                .getExpirationTime());
        assertEquals(TokenType.APIKEY, tokens.getContent().get(2).getTokenType());
    }

    @Test
    public void testDelete() throws Exception {
        TokenService.delete(context, "0fd9ef0a-d8dc-46e7-bc84-0a8c100a25d0");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("NotFoundException: Requested token does not exist");
        TokenService.delete(context, "00000000-0000-0000-0000-000000000000");
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return TokenServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + Constants.Token.ENDPOINT_PATH)
    public static class TokenServiceResource extends AbstractNLICServiceResource {

        public TokenServiceResource() {
            super("token");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            final String targetTokenType = formParams.getFirst(Constants.Token.TOKEN_TYPE);
            if (TokenType.SHOP.name().equals(targetTokenType)
                    && !formParams.containsKey(Constants.Licensee.LICENSEE_NUMBER)) {
                return errorResponse("MalformedRequestException", "Malformed token request",
                        "TokenValidation", "Property 'licenseeNumber' not found");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.NUMBER, UUID.randomUUID().toString());
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            defaultPropertyValues.put(Constants.Token.TOKEN_TYPE, TokenType.DEFAULT.name());
            defaultPropertyValues.put(Constants.Token.TOKEN_PROP_VENDORNUMBER, "VDEMO");
            if (!TokenType.APIKEY.name().equals(targetTokenType)) {
                defaultPropertyValues.put(Constants.Token.EXPIRATION_TIME,
                        DateUtils.printDate(DateUtils.getCurrentDate()));
            }
            if (TokenType.SHOP.name().equals(targetTokenType)) {
                defaultPropertyValues.put(Constants.Token.TOKEN_PROP_SHOP_URL,
                        "https://go.netlicensing.io/shop/v2/?shoptoken=" +
                                defaultPropertyValues.get(Constants.NUMBER));
            }

            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String productNumber, final UriInfo uriInfo) {
            return delete(productNumber, "0fd9ef0a-d8dc-46e7-bc84-0a8c100a25d0", null);
        }
    }

}
