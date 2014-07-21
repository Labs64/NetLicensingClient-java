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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.Property;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Integration tests for {@link LicenseService}.
 */
public class LicenseServiceTest extends BaseServiceTest {

    private static final String LICENSE_CUSTOM_PROPERTY = "CustomProperty";

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
        final License newLicense = new LicenseImpl();
        newLicense.setNumber("LC001-TEST");
        newLicense.setActive(false);
        newLicense.setName("Concrete Test License");
        newLicense.setPrice(new BigDecimal(20));
        newLicense.setCurrency(Currency.EUR);
        newLicense.setHidden(false);
        newLicense.addProperty(LICENSE_CUSTOM_PROPERTY, "Custom property value");

        final License createdLicense = LicenseService.create(context, "L001-TEST", "LT001-TEST", null, newLicense);

        assertNotNull(createdLicense);
        assertEquals("LC001-TEST", createdLicense.getNumber());
        assertEquals(false, createdLicense.getActive());
        assertEquals("Concrete Test License", createdLicense.getName());
        assertEquals(new BigDecimal("10.00"), createdLicense.getPrice());
        assertEquals(Currency.EUR, createdLicense.getCurrency());
        assertEquals(true, createdLicense.getHidden());
        assertEquals("L001-TEST", createdLicense.getLicensee().getNumber());
        assertEquals("LT001-TEST", createdLicense.getLicenseTemplate().getNumber());
        assertEquals("Custom property value", createdLicense.getLicenseProperties().get(LICENSE_CUSTOM_PROPERTY));
    }

    @Test
    public void testCreateEmpty() throws Exception {
        final License newLicense = new LicenseImpl();
        final License createdLicense = LicenseService.create(context, "L001-TEST", "LT001-TEST", null, newLicense);

        assertNotNull(createdLicense);
        assertEquals(true, createdLicense.getActive());
        assertEquals("Test License", createdLicense.getName());
        assertEquals(new BigDecimal("10.00"), createdLicense.getPrice());
        assertEquals(Currency.EUR, createdLicense.getCurrency());
        assertEquals(true, createdLicense.getHidden());
        assertEquals("L001-TEST", createdLicense.getLicensee().getNumber());
        assertEquals("LT001-TEST", createdLicense.getLicenseTemplate().getNumber());
    }

    @Test
    public void testCreateWithoutLicenseeNumber() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Licensee number is not provided");
        LicenseService.create(context, null, null, null, new LicenseImpl());
    }

    @Test
    public void testCreateWithoutLicenseTemplateNumber() throws Exception {
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: License template number is not provided");
        LicenseService.create(context, "L001-TEST", null, null, new LicenseImpl());
    }

    @Test
    public void testGet() throws Exception {
        final License license = LicenseService.get(context, "LC001-TEST");

        assertNotNull(license);
        assertEquals("LC001-TEST", license.getNumber());
        assertEquals(true, license.getActive());
        assertEquals("Concrete Test License", license.getName());
        assertEquals(new BigDecimal("15.00"), license.getPrice());
        assertEquals(Currency.EUR, license.getCurrency());
        assertEquals(false, license.getHidden());
        assertEquals("L001-TEST", license.getLicensee().getNumber());
        assertEquals("LT001-TEST", license.getLicenseTemplate().getNumber());
        assertEquals("Custom property value", license.getLicenseProperties().get(LICENSE_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<License> licenses = LicenseService.list(context);

        assertNotNull(licenses);
        assertTrue(licenses.hasContent());
        assertEquals(3, licenses.getItemsNumber());
        assertEquals("LC001-TEST", licenses.getContent().get(0).getNumber());
        assertEquals("Test License 2", licenses.getContent().get(1).getName());
        assertEquals("LT002-TEST", licenses.getContent().get(2).getLicenseTemplate().getNumber());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return LicenseServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + LicenseService.CONTEXT_PATH)
    public static class LicenseServiceResource extends AbstractNLICServiceResource {

        public LicenseServiceResource() {
            super("license");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.Licensee.LICENSEE_NUMBER)) {
                return errorResponse("MalformedRequestException", "Licensee number is not provided");
            }
            if (!formParams.containsKey(Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER)) {
                return errorResponse("MalformedRequestException", "License template number is not provided");
            }

            // properties that will be taken from template and can't be changed by user
            formParams.remove(Constants.PRICE);
            formParams.remove(Constants.CURRENCY);
            formParams.remove(Constants.License.HIDDEN);

            return create(formParams, getDefaultPropertyValuesFromLicenseTemplate());
        }

        private Map<String, String> getDefaultPropertyValuesFromLicenseTemplate() {
            final Map<String, String> values = new HashMap<String, String>();

            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE + "netlicensing-licensetemplate-get.xml",
                    Netlicensing.class);
            final List<Property> properties = netlicensing.getItems().getItem().get(0).getProperty();

            final String[] propertyNames = new String[] { Constants.ACTIVE, Constants.NAME, Constants.PRICE, Constants.CURRENCY };
            for (String propertyName : propertyNames) {
                values.put(propertyName, SchemaFunction.propertyByName(properties, propertyName).getValue());
            }

            final boolean hideLicenses = "true".equals(SchemaFunction.propertyByName(properties, Constants.LicenseTemplate.HIDE_LICENSES).getValue());
            values.put(Constants.License.HIDDEN, String.valueOf(hideLicenses));

            return values;
        }

    }

}
