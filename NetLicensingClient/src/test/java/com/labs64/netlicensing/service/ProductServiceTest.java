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
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.Property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link ProductService}.
 */
public class ProductServiceTest extends BaseServiceTest {

    private static final String PRODUCT_CUSTOM_PROPERTY = "CustomProperty";
    private static final String PRODUCT_DELETING_PROPERTY = "toBeDeleted";
    private static final String PRODUCT_NUMBER = "P001-TEST";
    private static final String PROMO_CODE = "PROMO-001";
    private static final String LICENSE_TEMPLATE_NUMBER = "LT-DISCOUNT-001";
    private static final String MIN_CART_TOTAL = "minCartTotal";
    private static final String PROMO_CODE_PROPERTY = "promoCode";

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @BeforeEach
    public void resetResourceState() {
        ProductServiceResource.lastProductNumber = null;
        ProductServiceResource.lastPromoCode = null;
        ProductServiceResource.lastCartTotal = null;
    }

    @Test
    public void testCreate() throws Exception {
        final Product newProduct = new ProductImpl();
        newProduct.setName("Test Product");
        newProduct.setNumber("P001-TEST");
        newProduct.setActive(true);
        newProduct.setVersion("v3.2");
        newProduct.setLicenseeAutoCreate(true);
        newProduct.setDescription("Test Product description");
        newProduct.addProperty(PRODUCT_CUSTOM_PROPERTY, "Test Value");

        final Product createdProduct = ProductService.create(context, newProduct);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertEquals("P001-TEST", createdProduct.getNumber());
        assertEquals(true, createdProduct.getActive());
        assertEquals("v3.2", createdProduct.getVersion());
        assertEquals(true, createdProduct.getLicenseeAutoCreate());
        assertEquals("Test Product description", createdProduct.getDescription());
        assertEquals("Test Value", createdProduct.getProperties().get(PRODUCT_CUSTOM_PROPERTY));
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final Product newProduct = new ProductImpl();
        final Exception e = assertThrows(ServiceException.class, () -> {
            ProductService.create(context, newProduct);
        });
        assertEquals("MalformedRequestException: Product name is required", e.getMessage());
    }

    @Test
    public void testCreateWithNameOnly() throws Exception {
        final Product newProduct = new ProductImpl();
        newProduct.setName("Test Product");

        final Product createdProduct = ProductService.create(context, newProduct);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertEquals(true, createdProduct.getActive());
        assertEquals("", createdProduct.getVersion());
        assertEquals(false, createdProduct.getLicenseeAutoCreate());
    }

    @Test
    public void testGet() throws Exception {
        final Product resultProduct = ProductService.get(context, "P014-TEST");

        assertNotNull(resultProduct);
        assertEquals("P014-TEST", resultProduct.getNumber());
        assertEquals("Product Numero Uno", resultProduct.getName());
        assertEquals("Licensed to Licensee", resultProduct.getLicensingInfo());
        assertEquals("CustomPropertyValue", resultProduct.getProperties().get(PRODUCT_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<Product> products = ProductService.list(context, null);

        assertNotNull(products);
        assertTrue(products.hasContent());
        assertEquals(3, products.getItemsNumber());
        assertEquals("P001-TEST", products.getContent().get(0).getNumber());
        assertEquals("Test Product 2", products.getContent().get(1).getName());
        assertEquals("v2.0", products.getContent().get(2).getVersion());
    }

    @Test
    public void testUpdate() throws Exception {
        final Product product = new ProductImpl();
        product.setName("Test Product");
        product.setNumber("P002-TEST");
        product.addProperty(PRODUCT_CUSTOM_PROPERTY, "Test Value");
        product.addProperty(PRODUCT_DELETING_PROPERTY, "");

        final Product updatedProduct = ProductService.update(context, "P001-TEST", product);

        assertNotNull(updatedProduct);
        assertEquals("Test Product", updatedProduct.getName());
        assertEquals("P002-TEST", updatedProduct.getNumber());
        assertEquals(false, updatedProduct.getActive());
        assertEquals("v1.0", updatedProduct.getVersion());
        assertEquals(true, updatedProduct.getLicenseeAutoCreate());
        assertEquals("Test Value", updatedProduct.getProperties().get(PRODUCT_CUSTOM_PROPERTY));
        assertNull(updatedProduct.getProperties().get(PRODUCT_DELETING_PROPERTY));
    }

    @Test
    public void testDelete() throws Exception {
        ProductService.delete(context, "P001-TEST", true);

        final Exception e = assertThrows(ServiceException.class, () -> {
            ProductService.delete(context, "P001-NONE", false);
        });
        assertEquals("NotFoundException: Requested product does not exist", e.getMessage());
    }

    @Test
    public void testResolveDiscount() throws Exception {
        final LicenseTemplate discount = ProductService.resolveDiscount(context, PRODUCT_NUMBER, PROMO_CODE);

        assertNotNull(discount);
        assertEquals(LICENSE_TEMPLATE_NUMBER, discount.getNumber());
        assertEquals("Promo discount", discount.getName());
        assertEquals(LicenseType.FEATURE, discount.getLicenseType());
        assertEquals("25", discount.getProperties().get("discountAmount"));
        assertEquals(PROMO_CODE, discount.getProperties().get(PROMO_CODE_PROPERTY));
        assertEquals(PRODUCT_NUMBER, ProductServiceResource.lastProductNumber);
        assertEquals(PROMO_CODE, ProductServiceResource.lastPromoCode);
        assertNull(ProductServiceResource.lastCartTotal);
    }

    @Test
    public void testResolveDiscountWithCartTotal() throws Exception {
        final LicenseTemplate discount = ProductService.resolveDiscount(context, PRODUCT_NUMBER, PROMO_CODE,
                new BigDecimal("100.50"));

        assertNotNull(discount);
        assertEquals(LICENSE_TEMPLATE_NUMBER, discount.getNumber());
        assertEquals("100.50", ProductServiceResource.lastCartTotal);
    }

    @Test
    public void testResolveDiscountReturnsNullWhenDiscountDoesNotExist() throws Exception {
        final LicenseTemplate discount = ProductService.resolveDiscount(context, PRODUCT_NUMBER, "UNKNOWN");

        assertNull(discount);
    }

    @Test
    public void testResolveDiscountRethrowsNonNotFoundServiceException() {
        final ServiceException ex = assertThrows(ServiceException.class,
                () -> ProductService.resolveDiscount(context, PRODUCT_NUMBER, "BROKEN"));

        assertEquals("IllegalOperationException: Discount resolve failed", ex.getMessage());
    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return ProductServiceResource.class;
    }

    @Path(REST_API_PATH + "/product")
    public static class ProductServiceResource extends AbstractNLICServiceResource {

        private static String lastProductNumber;
        private static String lastPromoCode;
        private static String lastCartTotal;

        public ProductServiceResource() {
            super("product");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("MalformedRequestException", "Product name is required");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.VERSION, "");
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            defaultPropertyValues.put(Constants.Product.LICENSEE_AUTO_CREATE, "false");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String productNumber, final UriInfo uriInfo) {
            return delete(productNumber, "P001-TEST", uriInfo.getQueryParameters());
        }

        @POST
        @Path("{productNumber}/discount/{promoCode}/resolve")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response resolveDiscount(@PathParam("productNumber") final String productNumber,
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

        private void addProperty(final Item item, final String name, final String value) {
            final Property property = new Property(value, name);
            item.getProperty().add(property);
        }
    }

}
