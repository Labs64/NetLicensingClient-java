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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.ProductModuleImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;

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
        newModule.setLicensingModel(Constants.LicensingModel.TimeLimitedEvaluation.NAME);

        final ProductModule createdModule = ProductModuleService.create(context, "P001-TEST", newModule);

        assertNotNull(createdModule);
        assertEquals("PM001-TEST", createdModule.getNumber());
        assertEquals(true, createdModule.getActive());
        assertEquals("Test Product Module", createdModule.getName());
        assertEquals(Constants.LicensingModel.TimeLimitedEvaluation.NAME, createdModule.getLicensingModel());
        assertEquals("P001-TEST", createdModule.getProduct().getNumber());
    }

    @Test
    public void testCreateWithoutProductNumber() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Product number is not provided");
        ProductModuleService.create(context, null, new ProductModuleImpl());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Product module name is required");

        final ProductModule newModule = new ProductModuleImpl();
        ProductModuleService.create(context, "P001-TEST", newModule);
    }

    @Test
    public void testCreateWithRequiredPropertiesOnly() throws Exception {
        final ProductModule newModule = new ProductModuleImpl();
        newModule.setName("Test Product Module");
        newModule.setLicensingModel(Constants.LicensingModel.TimeLimitedEvaluation.NAME);

        final ProductModule createdModule = ProductModuleService.create(context, "P001-TEST", newModule);

        assertNotNull(createdModule);
        assertEquals(true, createdModule.getActive());
    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return ProductModuleServiceResource.class;
    }

    @Path(REST_API_PATH)
    public static class ProductModuleServiceResource extends AbstractNLICServiceResource {

        public ProductModuleServiceResource() {
            super("productModule");
        }

        @Path("productmodule")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response createProductModule(final MultivaluedMap<String, String> formParams) {

            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product number is not provided");
            }
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("MalformedRequestException", "Product module name is required");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }
    }

}
