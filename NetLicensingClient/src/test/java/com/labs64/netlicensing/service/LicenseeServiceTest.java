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

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.domain.vo.WarningLevel;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeAll
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
        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseeService.create(context, null, new LicenseeImpl());
        });
        assertEquals("MalformedRequestException: Product number is not provided", e.getMessage());
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

        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseeService.delete(context, "L001-NONE", false);
        });
        assertEquals("NotFoundException: Requested licensee does not exist", e.getMessage());
    }

    @Test
    public void testValidate() throws Exception {

        final ValidationParameters validationParameters = new ValidationParameters();
        validationParameters.setLicenseeName("Test Licensee");
        validationParameters.setProductNumber(productNumber);
        validationParameters.setLicenseeProperty("customProperty", "Licensee Custom Property");
        final ValidationResult result = LicenseeService.validate(context, licenseeNumber, validationParameters);

        assertNotNull(result);
        assertNotNull(result.getTtl());

        final Composition validation = result.getProductModuleValidation("M001-TEST");
        assertNotNull(validation);
        assertEquals("FeatureWithTimeVolume", validation.getProperties().get(Constants.ProductModule.LICENSING_MODEL)
                .getValue());
        assertEquals("Test module", validation.getProperties().get(Constants.ProductModule.PRODUCT_MODULE_NAME)
                .getValue());
        assertTrue(Boolean.parseBoolean(validation.getProperties().get("LIST1").getProperties()
                .get(Constants.LicensingModel.VALID).getValue()));
        assertEquals(
                WarningLevel.GREEN,
                WarningLevel.parseString(validation.getProperties().get("LIST2").getProperties()
                        .get(Constants.ValidationResult.WARNING_LEVEL).getValue()));
    }

    @Test
    public void testOfflineValidation() throws Exception {
        final ValidationParameters validationParameters = new ValidationParameters();
        validationParameters.setLicenseeName("Test Licensee");
        validationParameters.setProductNumber(productNumber);
        validationParameters.setLicenseeProperty("customProperty", "Licensee Custom Property");
        validationParameters.setForOfflineUse(true);
        final ValidationResult result = LicenseeService.validate(context, licenseeNumber, validationParameters);

        assertEquals(licenseeNumber, result.getLicensee().getNumber());

        final Composition validation = result.getProductModuleValidation("M001-TEST");
        assertNotNull(validation);
        assertEquals("FeatureWithTimeVolume", validation.getProperties().get(Constants.ProductModule.LICENSING_MODEL)
                .getValue());
        assertEquals("Test module", validation.getProperties().get(Constants.ProductModule.PRODUCT_MODULE_NAME)
                .getValue());
        assertTrue(Boolean.parseBoolean(validation.getProperties().get("LIST1").getProperties()
                .get(Constants.LicensingModel.VALID).getValue()));
        assertEquals(
                WarningLevel.GREEN,
                WarningLevel.parseString(validation.getProperties().get("LIST2").getProperties()
                        .get(Constants.ValidationResult.WARNING_LEVEL).getValue()));
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

    @Path(REST_API_PATH + "/licensee")
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
                @FormParam("licenseeName") final String licenseeName,
                @FormParam("customProperty") final String licenseeCustomProperty,
                @FormParam("forOfflineUse") final String forOfflineUse) {

            if (!productNumber.equals(productNumber)) {
                return unexpectedValueErrorResponse("productNumber");
            }
            if (!"Test Licensee".equals(licenseeName)) {
                return unexpectedValueErrorResponse("licenseeName");
            }

            if (!"Licensee Custom Property".equals(licenseeCustomProperty)) {
                return unexpectedValueErrorResponse("customProperty");
            }

            final String validationFile = (Boolean.parseBoolean(forOfflineUse))
                    ? "netlicensing-licensee-validate-offline.xml"
                    : "netlicensing-licensee-validate.xml";

            try {
                final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE
                        + validationFile, Netlicensing.class);
                return Response.ok(netlicensing).build();
            } catch (JAXBException e) {
                return Response.serverError().entity("Exception in mocked server: " + e.getMessage()).build();
            }
        }

        /**
         * Mock for "transfer licensee" service.
         *
         * @param licenseeNumber
         *            licensee number
         * @param transferLicensee
         *            transfer licensee number
         * @return response with XML representation of transfer result
         */
        @POST
        @Path("{licenseeNumber}/transfer")
        public Response transferLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                @FormParam("transfer") final String transferLicensee) {

            try {
                final Netlicensing netlicensing = JAXBUtils
                        .readObject(TEST_CASE_BASE + "netlicensing-licensee-transfer.xml",
                                Netlicensing.class);
                return Response.ok(netlicensing).build();
            } catch (JAXBException e) {
                return Response.serverError().entity("Exception in mocked server: " + e.getMessage()).build();
            }
        }
    }

}
