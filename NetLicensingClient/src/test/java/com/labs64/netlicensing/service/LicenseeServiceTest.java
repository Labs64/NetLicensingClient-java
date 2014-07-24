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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.LicenseeImpl;
import com.labs64.netlicensing.domain.entity.ValidationResult;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Integration tests for {@link LicenseeService}.
 */
public class LicenseeServiceTest extends BaseServiceTest {

    private static final String LICENSEE_CUSTOM_PROPERTY = "CustomProperty";
    private static final String LICENSEE_DELETING_PROPERTY = "toBeDeleted";

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
        final Licensee newLicensee = new LicenseeImpl();
        newLicensee.setNumber("L001-TEST");
        newLicensee.setActive(false);

        final Licensee createdLicensee = LicenseeService.create(context, "P001-TEST", newLicensee);

        assertNotNull(createdLicensee);
        assertEquals("L001-TEST", createdLicensee.getNumber());
        assertEquals(false, createdLicensee.getActive());
        assertEquals("P001-TEST", createdLicensee.getProduct().getNumber());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final Licensee newLicensee = new LicenseeImpl();
        final Licensee createdLicensee = LicenseeService.create(context, "P001-TEST", newLicensee);

        assertNotNull(createdLicensee);
        assertEquals(true, createdLicensee.getActive());
        assertEquals("P001-TEST", createdLicensee.getProduct().getNumber());
    }

    @Test
    public void testCreateWithoutProductNumber() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Product number is not provided");
        LicenseeService.create(context, null, new LicenseeImpl());
    }

    @Test
    public void testGet() throws Exception {
        final Licensee licensee = LicenseeService.get(context, "L001-TEST");

        assertNotNull(licensee);
        assertEquals("L001-TEST", licensee.getNumber());
        assertEquals(true, licensee.getActive());
        assertEquals("P001-TEST", licensee.getProduct().getNumber());
        assertEquals("Custom property value", licensee.getLicenseeProperties().get(LICENSEE_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<Licensee> licensees = LicenseeService.list(context);

        assertNotNull(licensees);
        assertTrue(licensees.hasContent());
        assertEquals(3, licensees.getItemsNumber());
        assertEquals("L001-TEST", licensees.getContent().get(0).getNumber());
        assertEquals(true, licensees.getContent().get(1).getActive());
        assertEquals("P001-TEST", licensees.getContent().get(2).getProduct().getNumber());
    }

    @Test
    public void testUpdate() throws Exception {
        final Licensee licensee = new LicenseeImpl();
        licensee.setNumber("L002-TEST");
        licensee.setActive(true);
        licensee.addProperty(LICENSEE_CUSTOM_PROPERTY, "New property value");
        licensee.addProperty(LICENSEE_DELETING_PROPERTY, "");

        final Licensee updatedLicensee = LicenseeService.update(context, "L001-TEST", licensee);

        assertNotNull(updatedLicensee);
        assertEquals("L002-TEST", updatedLicensee.getNumber());
        assertEquals(true, updatedLicensee.getActive());
        assertEquals("P001-TEST", updatedLicensee.getProduct().getNumber());
        assertEquals("New property value", updatedLicensee.getLicenseeProperties().get(LICENSEE_CUSTOM_PROPERTY));
        assertNull(updatedLicensee.getLicenseeProperties().get(LICENSEE_DELETING_PROPERTY));
    }

    @Test
    public void testDelete() throws Exception {
        LicenseeService.delete(context, "L001-TEST", true);

        thrown.expect(RestException.class);
        thrown.expectMessage("NotFoundException: Requested licensee does not exist");
        LicenseeService.delete(context, "L001-NONE", false);
    }

    @Test
    public void testValidate() throws Exception {
        final ValidationResult result = LicenseeService.validate(context, "L001-TEST", "P001-TEST", "Test Licensee");

        assertNotNull(result);

        final Composition validation = result.getProductModuleValidation("M001-TEST");
        assertNotNull(validation);
        assertEquals("FeatureWithTimeVolume", validation.getProperties().get(Constants.ProductModule.LICENSING_MODEL).getValue());
        assertEquals("Test module", validation.getProperties().get(Constants.ProductModule.PRODUCT_MODULE_NAME).getValue());
        assertEquals("true", validation.getProperties().get("LIST1").getProperties().get(Constants.LicensingModel.FeatureWithTimeVolume.VALID).getValue());
        assertEquals("green", validation.getProperties().get("LIST2").getProperties().get(Constants.LicensingModel.FeatureWithTimeVolume.EXPIRATION_WARNING_LEVEL).getValue());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return LicenseeServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + LicenseeService.CONTEXT_PATH)
    public static class LicenseeServiceResource extends AbstractNLICServiceResource {

        public LicenseeServiceResource() {
            super("licensee");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product number is not provided");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String licenseeNumber, final UriInfo uriInfo) {
            return delete(licenseeNumber, "L001-TEST", uriInfo.getQueryParameters());
        }

        /**
         * Mock for "validate licensee" service.
         *
         * @param licenseeNumber
         *            licensee number
         * @param productNumber
         *            product number
         * @param licenseeName
         *            licensee name
         * @return response with XML representation of validation result
         */
        @GET
        @Path("{licenseeNumber}/validate")
        public Response validateLicensee(@PathParam("licenseeNumber") final String licenseeNumber, @QueryParam("productNumber") final String productNumber,
                @QueryParam("name") final String licenseeName) {

            if (!"P001-TEST".equals(productNumber)) {
                return unexpectedValueErrorResponse("productNumber");
            }
            if (!"Test Licensee".equals(licenseeName)) {
                return unexpectedValueErrorResponse("licenseeName");
            }

            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE
                    + "netlicensing-licensee-validate.xml", Netlicensing.class);
            return Response.ok(netlicensing).build();
        }
    }
}
