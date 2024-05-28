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

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link LicenseTemplateService}.
 */
public class LicenseTemplateServiceTest extends BaseServiceTest {

    private static final String TIME_VOLUME_PROPERTY = "timeVolume";

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testCreate() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setNumber("LT001-TEST");
        licenseTemplate.setName("Test License Template");
        licenseTemplate.setLicenseType(LicenseType.TIMEVOLUME);
        licenseTemplate.setActive(false);
        licenseTemplate.setCurrency(Currency.EUR);
        licenseTemplate.setPrice(new BigDecimal("10.5"));
        licenseTemplate.setAutomatic(true);
        licenseTemplate.setHidden(true);
        licenseTemplate.setHideLicenses(true);
        licenseTemplate.addProperty(TIME_VOLUME_PROPERTY, "30");

        final LicenseTemplate createdTemplate = LicenseTemplateService.create(context, "PM001-TEST", licenseTemplate);

        assertNotNull(createdTemplate);
        assertEquals("LT001-TEST", createdTemplate.getNumber());
        assertEquals("Test License Template", createdTemplate.getName());
        assertEquals(LicenseType.TIMEVOLUME, createdTemplate.getLicenseType());
        assertEquals(false, createdTemplate.getActive());
        assertEquals(Currency.EUR, createdTemplate.getCurrency());
        assertEquals(new BigDecimal("10.50"), createdTemplate.getPrice());
        assertEquals(true, createdTemplate.getAutomatic());
        assertEquals(true, createdTemplate.getHidden());
        assertEquals(true, createdTemplate.getHideLicenses());
        assertEquals("PM001-TEST", createdTemplate.getProductModule().getNumber());
        assertEquals("30", createdTemplate.getProperties().get(TIME_VOLUME_PROPERTY));
    }

    @Test
    public void testCreateWithoutProductModuleNumber() throws Exception {
        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.create(context, null, new LicenseTemplateImpl());
        });
        assertEquals("MalformedRequestException: Product module number is not provided", e.getMessage());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.create(context, "PM001-TEST", licenseTemplate);
        });
        assertEquals("MalformedRequestException: License template name is required", e.getMessage());
    }

    @Test
    public void testCreateWithRequiredPropertiesOnly() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setName("Test License Template");
        licenseTemplate.setLicenseType(LicenseType.FEATURE);

        final LicenseTemplate createdTemplate = LicenseTemplateService.create(context, "PM001-TEST", licenseTemplate);

        assertNotNull(createdTemplate);
        assertEquals(true, createdTemplate.getActive());
        assertEquals(false, createdTemplate.getAutomatic());
        assertEquals(false, createdTemplate.getHidden());
        assertEquals(false, createdTemplate.getHideLicenses());
    }

    @Test
    public void testCreateWithPriceAndWithoutCurrency() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setName("Test License Template");
        licenseTemplate.setLicenseType(LicenseType.FEATURE);
        licenseTemplate.setPrice(new BigDecimal("10"));

        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.create(context, "PM001-TEST", licenseTemplate);
        });
        assertEquals("MalformedRequestException: 'price' field must be accompanied with the 'currency' field",
                e.getMessage());
    }

    @Test
    public void testCreateWithCurrencyAndWithoutPrice() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setName("Test License Template");
        licenseTemplate.setLicenseType(LicenseType.FEATURE);
        licenseTemplate.setCurrency(Currency.EUR);

        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.create(context, "PM001-TEST", licenseTemplate);
        });
        assertEquals("MalformedRequestException: 'currency' field can not be used without the 'price' field",
                e.getMessage());
    }

    @Test
    public void testGet() throws Exception {
        final LicenseTemplate licenseTemplate = LicenseTemplateService.get(context, "LT001-TEST");

        assertNotNull(licenseTemplate);
        assertEquals("LT001-TEST", licenseTemplate.getNumber());
        assertEquals("Test License", licenseTemplate.getName());
        assertEquals(LicenseType.FEATURE, licenseTemplate.getLicenseType());
        assertEquals(true, licenseTemplate.getActive());
        assertEquals(Currency.EUR, licenseTemplate.getCurrency());
        assertEquals(new BigDecimal("10.00"), licenseTemplate.getPrice());
        assertEquals(true, licenseTemplate.getAutomatic());
        assertEquals(true, licenseTemplate.getHidden());
        assertEquals(true, licenseTemplate.getHideLicenses());
        assertEquals("PM001-TEST", licenseTemplate.getProductModule().getNumber());
        assertEquals("30", licenseTemplate.getProperties().get(TIME_VOLUME_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<LicenseTemplate> licenseTemplates = LicenseTemplateService.list(context, null);

        assertNotNull(licenseTemplates);
        assertTrue(licenseTemplates.hasContent());
        assertEquals(3, licenseTemplates.getItemsNumber());

        final LicenseTemplate template1 = licenseTemplates.getContent().get(0);
        assertEquals("LT001-TEST", template1.getNumber());
        assertEquals(LicenseType.FEATURE, template1.getLicenseType());

        final LicenseTemplate template2 = licenseTemplates.getContent().get(1);
        assertEquals("Time Volume License Template", template2.getName());
        assertEquals(LicenseType.TIMEVOLUME, template2.getLicenseType());
        assertEquals("30", template2.getProperties().get(TIME_VOLUME_PROPERTY));

        final LicenseTemplate template3 = licenseTemplates.getContent().get(2);
        assertEquals("3 sessions floating", template3.getName());
        assertEquals(LicenseType.FLOATING, template3.getLicenseType());
        assertEquals("3", template3.getProperties().get("maxSessions"));
    }

    @Test
    public void testUpdate() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setNumber("LT002-TEST");
        licenseTemplate.setLicenseType(LicenseType.FEATURE);
        licenseTemplate.setPrice(new BigDecimal(15));
        licenseTemplate.setCurrency(Currency.EUR);
        licenseTemplate.addProperty(TIME_VOLUME_PROPERTY, "");

        final LicenseTemplate updatedTemplate = LicenseTemplateService.update(context, "LT001-TEST", licenseTemplate);

        assertNotNull(updatedTemplate);
        assertEquals("LT002-TEST", updatedTemplate.getNumber());
        assertEquals("Test License Template", updatedTemplate.getName());
        assertEquals(LicenseType.FEATURE, updatedTemplate.getLicenseType());
        assertEquals(true, updatedTemplate.getActive());
        assertEquals(Currency.EUR, updatedTemplate.getCurrency());
        assertEquals(new BigDecimal("15.00"), updatedTemplate.getPrice());
        assertEquals(false, updatedTemplate.getAutomatic());
        assertEquals(false, updatedTemplate.getHidden());
        assertEquals(false, updatedTemplate.getHideLicenses());
        assertEquals("PM001-TEST", updatedTemplate.getProductModule().getNumber());
        assertNull(updatedTemplate.getProperties().get(TIME_VOLUME_PROPERTY));
    }

    @Test
    public void testUpdateLicenseTypeToTimeVolume() throws Exception {
        final LicenseTemplate licenseTemplate = new LicenseTemplateImpl();
        licenseTemplate.setLicenseType(LicenseType.TIMEVOLUME);

        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.update(context, "LT001-TEST", licenseTemplate);
        });
        assertEquals(
                "IllegalOperationException: License template of type 'TIMEVOLUME' must have property 'timeVolume' specified.",
                e.getMessage());
    }

    @Test
    public void testDelete() throws Exception {
        LicenseTemplateService.delete(context, "LT001-TEST", true);

        final Exception e = assertThrows(ServiceException.class, () -> {
            LicenseTemplateService.delete(context, "PM001-NONE", false);
        });
        assertEquals("NotFoundException: Requested license template does not exist", e.getMessage());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return LicenseTemplateServiceResource.class;
    }

    @Path(REST_API_PATH + "/licensetemplate")
    public static class LicenseTemplateServiceResource extends AbstractNLICServiceResource {

        public LicenseTemplateServiceResource() {
            super("licenseTemplate");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.ProductModule.PRODUCT_MODULE_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product module number is not provided");
            }
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("MalformedRequestException", "License template name is required");
            }
            if (formParams.containsKey(Constants.PRICE) && !formParams.containsKey(Constants.CURRENCY)) {
                return errorResponse("MalformedRequestException",
                        "'price' field must be accompanied with the 'currency' field");
            }
            if (formParams.containsKey(Constants.CURRENCY) && !formParams.containsKey(Constants.PRICE)) {
                return errorResponse("MalformedRequestException",
                        "'currency' field can not be used without the 'price' field");
            }

            roundParamValueToTwoDecimalPlaces(formParams, Constants.PRICE);

            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            defaultPropertyValues.put(Constants.LicenseTemplate.AUTOMATIC, "false");
            defaultPropertyValues.put(Constants.LicenseTemplate.HIDDEN, "false");
            defaultPropertyValues.put(Constants.LicenseTemplate.HIDE_LICENSES, "false");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response update(final String number, final MultivaluedMap<String, String> formParams) {
            final boolean isTimeVolume = LicenseType.TIMEVOLUME.value().equals(
                    formParams.getFirst(Constants.LicenseTemplate.LICENSE_TYPE));
            if (isTimeVolume && !formParams.containsKey(TIME_VOLUME_PROPERTY)) {
                return errorResponse("IllegalOperationException",
                        "License template of type 'TIMEVOLUME' must have property 'timeVolume' specified.");
            }

            roundParamValueToTwoDecimalPlaces(formParams, Constants.PRICE);
            return super.update(number, formParams);
        }

        @Override
        public Response delete(final String number, final UriInfo uriInfo) {
            return delete(number, "LT001-TEST", uriInfo.getQueryParameters());
        }
    }

}
