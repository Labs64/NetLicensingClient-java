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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;

/**
 * Integration tests for {@link ProductModuleService}.
 */
public class ProductModuleServiceTest extends BaseServiceTest {

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
        final ProductModule newModule = new ProductModuleImpl();
        newModule.setNumber("PM001-TEST");
        newModule.setActive(true);
        newModule.setName("Test Product Module");
        newModule.setLicensingModel(Constants.LicensingModel.Rental.NAME);
        newModule.addProperty(Constants.LicensingModel.Rental.YELLOW_THRESHOLD, "10");
        newModule.addProperty(Constants.LicensingModel.Rental.RED_THRESHOLD, "3");

        final ProductModule createdModule = ProductModuleService.create(context, "P001-TEST", newModule);

        assertNotNull(createdModule);
        assertEquals("PM001-TEST", createdModule.getNumber());
        assertEquals(true, createdModule.getActive());
        assertEquals("Test Product Module", createdModule.getName());
        assertEquals(Constants.LicensingModel.Rental.NAME, createdModule.getLicensingModel());
        assertEquals("P001-TEST", createdModule.getProduct().getNumber());
        assertEquals("10", createdModule.getProperties().get(Constants.LicensingModel.Rental.YELLOW_THRESHOLD));
        assertEquals("3", createdModule.getProperties().get(Constants.LicensingModel.Rental.RED_THRESHOLD));
    }

    @Test
    public void testCreateWithoutProductNumber() throws Exception {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("MalformedRequestException: Product number is not provided");
        ProductModuleService.create(context, null, new ProductModuleImpl());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("MalformedRequestException: Product module name is required");

        final ProductModule newModule = new ProductModuleImpl();
        ProductModuleService.create(context, "P001-TEST", newModule);
    }

    @Test
    public void testCreateWithRequiredPropertiesOnly() throws Exception {
        final ProductModule newModule = new ProductModuleImpl();
        newModule.setName("Test Product Module");
        newModule.setLicensingModel(Constants.LicensingModel.TryAndBuy.NAME);

        final ProductModule createdModule = ProductModuleService.create(context, "P001-TEST", newModule);

        assertNotNull(createdModule);
        assertEquals(true, createdModule.getActive());
    }

    @Test
    public void testGet() throws Exception {
        final ProductModule productModule = ProductModuleService.get(context, "PM001-TEST");

        assertNotNull(productModule);
        assertEquals("PM001-TEST", productModule.getNumber());
        assertEquals(true, productModule.getActive());
        assertEquals("Test Product Module", productModule.getName());
        assertEquals("P001-TEST", productModule.getProduct().getNumber());
    }

    @Test
    public void testList() throws Exception {
        final Page<ProductModule> productModules = ProductModuleService.list(context, null);

        assertNotNull(productModules);
        assertTrue(productModules.hasContent());
        assertEquals(3, productModules.getItemsNumber());
        assertEquals("PM001-TEST", productModules.getContent().get(0).getNumber());
        assertEquals("Test module 2", productModules.getContent().get(1).getName());
        assertEquals(Constants.LicensingModel.Subscription.NAME, productModules.getContent().get(2).getLicensingModel());
    }

    @Test
    public void testUpdate() throws Exception {
        final ProductModule productModule = new ProductModuleImpl();
        productModule.setNumber("PM002-TEST");
        productModule.setName("Demo Product Module");
        productModule.addProperty(Constants.LicensingModel.Rental.RED_THRESHOLD, "5");

        final ProductModule updatedModule = ProductModuleService.update(context, "PM001-TEST", productModule);

        assertNotNull(updatedModule);
        assertEquals("PM002-TEST", updatedModule.getNumber());
        assertEquals(true, updatedModule.getActive());
        assertEquals("Demo Product Module", updatedModule.getName());
        assertEquals(Constants.LicensingModel.Rental.NAME, updatedModule.getLicensingModel());
        assertEquals("P001-TEST", updatedModule.getProduct().getNumber());
        assertEquals("10", updatedModule.getProperties().get(Constants.LicensingModel.Rental.YELLOW_THRESHOLD));
        assertEquals("5", updatedModule.getProperties().get(Constants.LicensingModel.Rental.RED_THRESHOLD));
    }

    @Test
    public void testDelete() throws Exception {
        ProductModuleService.delete(context, "PM001-TEST", true);

        thrown.expect(ServiceException.class);
        thrown.expectMessage("NotFoundException: Requested product module does not exist");
        ProductModuleService.delete(context, "PM001-NONE", false);
    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return ProductModuleServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + Constants.ProductModule.ENDPOINT_PATH)
    public static class ProductModuleServiceResource extends AbstractNLICServiceResource {

        public ProductModuleServiceResource() {
            super("productModule");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product number is not provided");
            }
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("MalformedRequestException", "Product module name is required");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String productModuleNumber, final UriInfo uriInfo) {
            return delete(productModuleNumber, "PM001-TEST", uriInfo.getQueryParameters());
        }

    }

}
