/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.service;

import java.math.BigDecimal;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;
import com.labs64.netlicensing.schema.context.Property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for {@link DiscountService}.
 */
public class DiscountServiceTest extends BaseServiceTest {

    private static final String PRODUCT_NUMBER = "P001-TEST";
    private static final String PROMO_CODE = "PROMO-001";
    private static final String LICENSE_TEMPLATE_NUMBER = "LT-DISCOUNT-001";
    private static final String MIN_CART_TOTAL = "minCartTotal";
    private static final String PROMO_CODE_PROPERTY = "promoCode";

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @BeforeEach
    public void resetResourceState() {
        DiscountServiceResource.lastProductNumber = null;
        DiscountServiceResource.lastPromoCode = null;
        DiscountServiceResource.lastCartTotal = null;
    }

    @Test
    public void testResolve() throws Exception {
        final LicenseTemplate discount = DiscountService.resolve(context, PRODUCT_NUMBER, PROMO_CODE);

        assertNotNull(discount);
        assertEquals(LICENSE_TEMPLATE_NUMBER, discount.getNumber());
        assertEquals("Promo discount", discount.getName());
        assertEquals(LicenseType.FEATURE, discount.getLicenseType());
        assertEquals("25", discount.getProperties().get("discountAmount"));
        assertEquals(PROMO_CODE, discount.getProperties().get(PROMO_CODE_PROPERTY));
        assertEquals(PRODUCT_NUMBER, DiscountServiceResource.lastProductNumber);
        assertEquals(PROMO_CODE, DiscountServiceResource.lastPromoCode);
        assertNull(DiscountServiceResource.lastCartTotal);
    }

    @Test
    public void testResolveWithCartTotal() throws Exception {
        final LicenseTemplate discount = DiscountService.resolve(context, PRODUCT_NUMBER, PROMO_CODE,
                new BigDecimal("100.50"));

        assertNotNull(discount);
        assertEquals(LICENSE_TEMPLATE_NUMBER, discount.getNumber());
        assertEquals("100.50", DiscountServiceResource.lastCartTotal);
    }

    @Test
    public void testResolveReturnsNullWhenDiscountDoesNotExist() throws Exception {
        final LicenseTemplate discount = DiscountService.resolve(context, PRODUCT_NUMBER, "UNKNOWN");

        assertNull(discount);
    }

    @Test
    public void testResolveRethrowsNonNotFoundServiceException() {
        final ServiceException ex = assertThrows(ServiceException.class,
                () -> DiscountService.resolve(context, PRODUCT_NUMBER, "BROKEN"));

        assertEquals("IllegalOperationException: Discount resolve failed", ex.getMessage());
    }

    @Override
    protected Class<?> getResourceClass() {
        return DiscountServiceResource.class;
    }

    @Path(REST_API_PATH + "/product/{productNumber}/discount/{promoCode}/resolve")
    public static class DiscountServiceResource {

        private static String lastProductNumber;
        private static String lastPromoCode;
        private static String lastCartTotal;

        private final ObjectFactory objectFactory = new ObjectFactory();

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response resolve(@PathParam("productNumber") final String productNumber,
                @PathParam("promoCode") final String promoCode, final MultivaluedMap<String, String> formParams) {
            lastProductNumber = productNumber;
            lastPromoCode = promoCode;
            lastCartTotal = formParams.getFirst(Constants.Product.Discount.CART_TOTAL);

            if ("UNKNOWN".equals(promoCode)) {
                return notFoundResponse();
            }
            if ("BROKEN".equals(promoCode)) {
                return errorResponse("IllegalOperationException", "Discount resolve failed");
            }

            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            netlicensing.setItems(objectFactory.createNetlicensingItems());

            final Item item = objectFactory.createItem();
            item.setType("LicenseTemplate");
            netlicensing.getItems().getItem().add(item);

            addProperty(item, Constants.NUMBER, LICENSE_TEMPLATE_NUMBER);
            addProperty(item, Constants.NAME, "Promo discount");
            addProperty(item, Constants.ACTIVE, Boolean.TRUE.toString());
            addProperty(item, Constants.LicenseTemplate.LICENSE_TYPE, LicenseType.FEATURE.value());
            addProperty(item, Constants.LicenseTemplate.AUTOMATIC, Boolean.FALSE.toString());
            addProperty(item, Constants.LicenseTemplate.HIDDEN, Boolean.TRUE.toString());
            addProperty(item, Constants.LicenseTemplate.HIDE_LICENSES, Boolean.TRUE.toString());
            addProperty(item, Constants.ProductModule.PRODUCT_MODULE_NUMBER, "PM-DISCOUNT-001");
            addProperty(item, PROMO_CODE_PROPERTY, promoCode);
            addProperty(item, "discountAmount", "25");
            addProperty(item, MIN_CART_TOTAL, "100");

            return Response.ok(netlicensing).build();
        }

        private Response notFoundResponse() {
            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            SchemaFunction.addInfo(netlicensing, "NotFoundException", InfoEnum.ERROR,
                    "Requested discount does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity(netlicensing).build();
        }

        private Response errorResponse(final String exceptionId, final String errorMessage) {
            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            SchemaFunction.addInfo(netlicensing, exceptionId, InfoEnum.ERROR, errorMessage);
            return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
        }

        private void addProperty(final Item item, final String name, final String value) {
            final Property property = new Property(value, name);
            item.getProperty().add(property);
        }
    }
}
