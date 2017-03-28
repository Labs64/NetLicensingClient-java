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

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Integration tests for {@link LicenseeService}.
 */
public class LicenseeServiceTest extends BaseServiceTest {

    private static final String LICENSEE_CUSTOM_PROPERTY = "CustomProperty";
    private static final String LICENSEE_DELETING_PROPERTY = "toBeDeleted";

    final String productNumber = "P001-TEST";
    final String licenseeNumber = "L001-TEST";

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
        newLicensee.setNumber(licenseeNumber);
        newLicensee.setActive(false);

        final Licensee createdLicensee = LicenseeService.create(context, productNumber, newLicensee);

        assertNotNull(createdLicensee);
        assertEquals(licenseeNumber, createdLicensee.getNumber());
        assertEquals(false, createdLicensee.getActive());
        assertEquals(productNumber, createdLicensee.getProduct().getNumber());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final Licensee newLicensee = new LicenseeImpl();
        final Licensee createdLicensee = LicenseeService.create(context, productNumber, newLicensee);

        assertNotNull(createdLicensee);
        assertEquals(true, createdLicensee.getActive());
        assertEquals(productNumber, createdLicensee.getProduct().getNumber());
    }

    @Test
    public void testCreateWithoutProductNumber() throws Exception {
        thrown.expect(ServiceException.class);
        thrown.expectMessage("MalformedRequestException: Product number is not provided");
        LicenseeService.create(context, null, new LicenseeImpl());
    }

    @Test
    public void testGet() throws Exception {
        final Licensee licensee = LicenseeService.get(context, licenseeNumber);

        assertNotNull(licensee);
        assertEquals(licenseeNumber, licensee.getNumber());
        assertEquals(true, licensee.getActive());
        assertEquals(productNumber, licensee.getProduct().getNumber());
        assertEquals("Custom property value", licensee.getProperties().get(LICENSEE_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<Licensee> licensees = LicenseeService.list(context, null);

        assertNotNull(licensees);
        assertTrue(licensees.hasContent());
        assertEquals(3, licensees.getItemsNumber());
        assertEquals(licenseeNumber, licensees.getContent().get(0).getNumber());
        assertEquals(true, licensees.getContent().get(1).getActive());
        assertEquals(productNumber, licensees.getContent().get(2).getProduct().getNumber());
    }

    @Test
    public void testUpdate() throws Exception {
        final Licensee licensee = new LicenseeImpl();
        licensee.setNumber("L002-TEST");
        licensee.setActive(true);
        licensee.addProperty(LICENSEE_CUSTOM_PROPERTY, "New property value");
        licensee.addProperty(LICENSEE_DELETING_PROPERTY, "");

        final Licensee updatedLicensee = LicenseeService.update(context, licenseeNumber, licensee);

        assertNotNull(updatedLicensee);
        assertEquals("L002-TEST", updatedLicensee.getNumber());
        assertEquals(true, updatedLicensee.getActive());
        assertEquals(productNumber, updatedLicensee.getProduct().getNumber());
        assertEquals("New property value", updatedLicensee.getProperties().get(LICENSEE_CUSTOM_PROPERTY));
        assertNull(updatedLicensee.getProperties().get(LICENSEE_DELETING_PROPERTY));
    }

    @Test
    public void testDelete() throws Exception {
        LicenseeService.delete(context, licenseeNumber, true);

        thrown.expect(ServiceException.class);
        thrown.expectMessage("NotFoundException: Requested licensee does not exist");
        LicenseeService.delete(context, "L001-NONE", false);
    }

    @Test
    public void testValidate() throws Exception {

        final ValidationParameters validationParameters = new ValidationParameters();
        validationParameters.setLicenseeName("Test Licensee");
        validationParameters.setProductNumber(productNumber);
        final ValidationResult result = LicenseeService.validate(context, licenseeNumber, validationParameters);

        assertNotNull(result);

        final Composition validation = result.getProductModuleValidation("M001-TEST");
        assertNotNull(validation);
        assertEquals("FeatureWithTimeVolume", validation.getProperties().get(Constants.ProductModule.LICENSING_MODEL)
                .getValue());
        assertEquals("Test module", validation.getProperties().get(Constants.ProductModule.PRODUCT_MODULE_NAME)
                .getValue());
        assertEquals(
                "true",
                validation.getProperties().get("LIST1").getProperties()
                .get(Constants.LicensingModel.VALID).getValue());
        assertEquals(
                "green",
                validation.getProperties().get("LIST2").getProperties()
                .get(Constants.LicensingModel.Rental.EXPIRATION_WARNING_LEVEL).getValue());
    }

    @Test
    public void testTransfer() throws Exception {
        final String sourceLicenseeNumber = "L002-TEST";

        final Licensee licensee = new LicenseeImpl();
        licensee.setNumber(licenseeNumber);
        licensee.setActive(true);
        LicenseeService.create(context, productNumber, licensee);

        final Licensee transferLicensee = new LicenseeImpl();
        transferLicensee.setNumber(sourceLicenseeNumber);
        transferLicensee.setActive(true);
        transferLicensee.addProperty(Constants.Licensee.PROP_MARKED_FOR_TRANSFER, Boolean.toString(true));
        LicenseeService.create(context, productNumber, transferLicensee);

        LicenseeService.transfer(context, licenseeNumber, sourceLicenseeNumber);
        // TODO(2K): test for exceptions
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return LicenseeServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + Constants.Licensee.ENDPOINT_PATH)
    public static class LicenseeServiceResource extends AbstractNLICServiceResource {

        public LicenseeServiceResource() {
            super("licensee");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product number is not provided");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<>();
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
        @POST
        @Path("{licenseeNumber}/validate")
        public Response validateLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                @FormParam("productNumber") final String productNumber,
                @FormParam("licenseeName") final String licenseeName) {

            if (!productNumber.equals(productNumber)) {
                return unexpectedValueErrorResponse("productNumber");
            }
            if (!"Test Licensee".equals(licenseeName)) {
                return unexpectedValueErrorResponse("licenseeName");
            }

            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE
                    + "netlicensing-licensee-validate.xml", Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

        /**
         * Mock for "transfer licensee" service.
         *
         * @param licenseeNumber
         *            licensee number
         * @param transfer
         *            transfer licensee number
         * @return response with XML representation of transfer result
         */
        @POST
        @Path("{licenseeNumber}/transfer")
        public Response transferLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                @FormParam("transfer") final String transferLicensee) {

            final Netlicensing netlicensing = JAXBUtils
                    .readObject(TEST_CASE_BASE + "netlicensing-licensee-transfer.xml",
                            Netlicensing.class);
            return Response.ok(netlicensing).build();
        }
    }

}
