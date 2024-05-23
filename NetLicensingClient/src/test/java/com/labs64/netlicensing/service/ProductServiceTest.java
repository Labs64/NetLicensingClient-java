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

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;

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

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
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
    }

}
