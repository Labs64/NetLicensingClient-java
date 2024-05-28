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

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.entity.Country;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link UtilityService}.
 */
public class UtilityServiceTest extends BaseServiceTest {

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testListLicensingModels() throws Exception {
        final Page<String> licensingModels = UtilityService.listLicensingModels(context);

        assertNotNull(licensingModels);
        assertTrue(licensingModels.hasContent());
        assertEquals(3, licensingModels.getContent().size());
        assertEquals("TimeLimitedEvaluation", licensingModels.getContent().get(0));
        assertEquals("TimeVolume", licensingModels.getContent().get(1));
        assertEquals("FeatureWithTimeVolume", licensingModels.getContent().get(2));
    }

    @Test
    public void testListLicenseTypes() throws Exception {
        final Page<String> licenseTypes = UtilityService.listLicenseTypes(context);

        assertNotNull(licenseTypes);
        assertTrue(licenseTypes.hasContent());
        assertEquals(2, licenseTypes.getContent().size());
        assertEquals("FEATURE", licenseTypes.getContent().get(0));
        assertEquals("TIMEVOLUME", licenseTypes.getContent().get(1));
    }

    @Test
    public void testListCountries() throws Exception {
        final Page<Country> countries = UtilityService.listCountries(context, null);

        assertNotNull(countries);
        assertTrue(countries.hasContent());
        assertEquals(3, countries.getItemsNumber());
        assertEquals("GERMANY", countries.getContent().get(1).getName());
        assertEquals(new BigDecimal("19"), countries.getContent().get(1).getVatPercent());
        assertEquals(true, countries.getContent().get(1).getIsEu());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return UtilityServiceResource.class;
    }

    @Path(REST_API_PATH + "/utility")
    public static class UtilityServiceResource {

        @GET
        @Path("licensingModels")
        public Response listLicensingModels() {
            return listFromResource("netlicensing-licensingModels-list.xml");
        }

        @GET
        @Path("licenseTypes")
        public Response listLicenseTypes() {
            return listFromResource("netlicensing-licenseTypes-list.xml");
        }

        @GET
        @Path("countries")
        public Response listCountries() {
            return listFromResource("netlicensing-countries-list.xml");
        }

        private Response listFromResource(final String resourceFileName) {
            final String xmlResourcePath = TEST_CASE_BASE + resourceFileName;
            try {
                final Netlicensing netlicensing = JAXBUtils.readObject(xmlResourcePath, Netlicensing.class);
                return Response.ok(netlicensing).build();
            } catch (JAXBException e) {
                return Response.serverError().entity("Exception in mocked server: " + e.getMessage()).build();
            }
        }

    }

}
