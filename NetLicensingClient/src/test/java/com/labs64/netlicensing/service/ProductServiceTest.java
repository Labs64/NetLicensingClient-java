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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.RestException;

/**
 * Integration tests for {@link ProductService}.
 */
public class ProductServiceTest extends BaseServiceTest {

    private static final String PRODUCT_CUSTOM_PROPERTY = "CustomProperty";

    // *** NLIC Tests ***

    private static Context context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        context = createContext();
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
        assertEquals("Test Value", createdProduct.getProductProperties().get(PRODUCT_CUSTOM_PROPERTY));
    }

    @Test
    public void testCreateEmpty() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Product name is required");

        final Product newProduct = new ProductImpl();
        ProductService.create(context, newProduct);
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
        assertEquals("CustomPropertyValue", resultProduct.getProductProperties().get(PRODUCT_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<Product> products = ProductService.list(context);

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

        final Product updatedProduct = ProductService.update(context, "P001", product);

        assertNotNull(updatedProduct);
        assertEquals("Test Product", updatedProduct.getName());
        assertEquals("P002-TEST", updatedProduct.getNumber());
        assertEquals(false, updatedProduct.getActive());
        assertEquals("v1.0", updatedProduct.getVersion());
        assertEquals(true, updatedProduct.getLicenseeAutoCreate());
        assertEquals("Test Value", updatedProduct.getProductProperties().get(PRODUCT_CUSTOM_PROPERTY));
    }

    @Test
    public void testDelete() throws Exception {
        ProductService.delete(context, "P001-TEST", true);

        thrown.expect(RestException.class);
        thrown.expectMessage("NotFoundException: Requested product does not exist");
        ProductService.delete(context, "P001-NONE", false);
    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return ProductServiceResource.class;
    }

    @Path(REST_API_PATH + "/product")
    public static class ProductServiceResource extends AbstractNLICServiceResource {

        public ProductServiceResource() {
            super("product");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("MalformedRequestException", "Product name is required");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.VERSION, "");
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            defaultPropertyValues.put(Constants.Product.LICENSEE_AUTO_CREATE, "false");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String productNumber, final boolean forceCascade) {
            return delete(productNumber, "P001-TEST", forceCascade);
        }
    }

}
