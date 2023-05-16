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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Bundle;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.impl.BundleImpl;
import com.labs64.netlicensing.domain.vo.BundleObtainParameters;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for {@link BundleService}.
 */
public class BundleServiceTest extends BaseServiceTest {
    private static Context context;

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testNameIsRequired() throws Exception {
        final Bundle bundle = new BundleImpl();
        bundle.setNumber("B001-TEST");
        bundle.setDescription("Description 1");
        bundle.setActive(true);
        bundle.setPrice(BigDecimal.valueOf(10));
        bundle.setCurrency(Currency.EUR);

        final Exception e = assertThrows(ServiceException.class, () -> {
            BundleService.create(context, bundle);
        });

        assertEquals("ValidationException: Bundle name is required.", e.getMessage());
    }

    @Test
    public void testPriceWithoutCurrency() throws Exception {
        final Bundle bundle = new BundleImpl();
        bundle.setName("Test Bundle");
        bundle.setPrice(BigDecimal.valueOf(10));

        final Exception e = assertThrows(ServiceException.class, () -> {
            BundleService.create(context, bundle);
        });
        assertEquals("IllegalArgumentException: 'price' field must be accompanied with the 'currency' field",
                e.getMessage());
    }

    @Test
    public void testCurrencyWithoutPrice() throws Exception {
        final Bundle bundle = new BundleImpl();
        bundle.setName("Test Bundle");
        bundle.setCurrency(Currency.EUR);

        final Exception e = assertThrows(ServiceException.class, () -> {
            BundleService.create(context, bundle);
        });
        assertEquals("IllegalArgumentException: 'currency' field can not be used without the 'price' field",
                e.getMessage());
    }

    @Test
    public void testCreate() throws Exception {
        final String name = "Bundle 1";
        final String number = "B001-TEST";
        final String description = "Description 1";
        final boolean active = true;
        final BigDecimal price = new BigDecimal(10);
        final Currency currency = Currency.EUR;
        final List<String> licenseTemplateNumbers = new ArrayList<>();
        licenseTemplateNumbers.add("LT001-TEST");
        licenseTemplateNumbers.add("LT002-TEST");

        final Bundle bundle = new BundleImpl();
        bundle.setName(name);
        bundle.setNumber(number);
        bundle.setDescription(description);
        bundle.setActive(active);
        bundle.setPrice(price);
        bundle.setCurrency(currency);
        bundle.setLicenseTemplateNumbers(licenseTemplateNumbers);

        final Bundle createdBundle = BundleService.create(context, bundle);

        assertNotNull(createdBundle);
        assertEquals(name, createdBundle.getName());
        assertEquals(number, createdBundle.getNumber());
        assertEquals(description, createdBundle.getDescription());
        assertEquals(active, createdBundle.getActive());
        assertEquals(price, createdBundle.getPrice());
        assertEquals(currency, createdBundle.getCurrency());
        assertEquals(licenseTemplateNumbers, createdBundle.getLicenseTemplateNumbers());
    }

    @Test
    public void testGet() throws Exception {
        final String name = "Bundle 1";
        final String number = "B001-TEST";
        final String description = "Description 1";
        final boolean active = true;
        final BigDecimal price = new BigDecimal(10);
        final Currency currency = Currency.EUR;
        final List<String> licenseTemplateNumbers = new ArrayList<>();
        licenseTemplateNumbers.add("LT001-TEST");
        licenseTemplateNumbers.add("LT002-TEST");

        final Bundle bundle = BundleService.get(context, "B014-TEST");
        assertNotNull(bundle);
        assertEquals(number, bundle.getNumber());
        assertEquals(name, bundle.getName());
        assertEquals(description, bundle.getDescription());
        assertEquals(active, bundle.getActive());
        assertEquals(price, bundle.getPrice());
        assertEquals(currency, bundle.getCurrency());
        assertEquals(licenseTemplateNumbers, bundle.getLicenseTemplateNumbers());
    }

    @Test
    public void testList() throws Exception {
        final Page<Bundle> bundles = BundleService.list(context, null);
        final List<String> licenseTemplateNumbers = new ArrayList<>();
        licenseTemplateNumbers.add("LT005-TEST");
        licenseTemplateNumbers.add("LT006-TEST");


        assertNotNull(bundles);
        assertTrue(bundles.hasContent());
        assertEquals(3, bundles.getItemsNumber());
        assertEquals("B001-TEST", bundles.getContent().get(0).getNumber());
        assertEquals("Bundle 2", bundles.getContent().get(1).getName());
        assertEquals(Currency.USD, bundles.getContent().get(1).getCurrency());
        assertEquals(BigDecimal.valueOf(30), bundles.getContent().get(2).getPrice());
        assertEquals(licenseTemplateNumbers, bundles.getContent().get(2).getLicenseTemplateNumbers());
    }

    @Test
    public void testUpdate() throws Exception {
        final String name = "Bundle 2";
        final String number = "B002-TEST";
        final String description = "Description 2";
        final boolean active = false;
        final BigDecimal price = new BigDecimal(20);
        final Currency currency = Currency.USD;
        final List<String> licenseTemplateNumbers = new ArrayList<>();
        licenseTemplateNumbers.add("LT003-TEST");
        licenseTemplateNumbers.add("LT004-TEST");

        final Bundle bundle = new BundleImpl();
        bundle.setName(name);
        bundle.setNumber(number);
        bundle.setDescription(description);
        bundle.setActive(active);
        bundle.setPrice(price);
        bundle.setCurrency(currency);
        bundle.setLicenseTemplateNumbers(licenseTemplateNumbers);

        final Bundle updatedBundle = BundleService.create(context, bundle);

        assertNotNull(updatedBundle);
        assertEquals(name, updatedBundle.getName());
        assertEquals(number, updatedBundle.getNumber());
        assertEquals(description, updatedBundle.getDescription());
        assertEquals(active, updatedBundle.getActive());
        assertEquals(price, updatedBundle.getPrice());
        assertEquals(currency, updatedBundle.getCurrency());
        assertEquals(licenseTemplateNumbers, updatedBundle.getLicenseTemplateNumbers());
    }

    @Test
    public void testDelete() throws Exception {
        BundleService.delete(context, "B001-TEST");

        final Exception e = assertThrows(ServiceException.class, () -> {
            BundleService.delete(context, "B001-NONE");
        });
        assertEquals("NotFoundException: Requested bundle does not exist", e.getMessage());
    }

    @Test
    public void testObtain() throws Exception {
        final BundleObtainParameters bundleObtainParameters = new BundleObtainParameters();
        bundleObtainParameters.setLicenseeNumber("LE001-TEST");
        
        final Page<License> licenses = BundleService.obtain(context, "B001-TEST", bundleObtainParameters);

        assertNotNull(licenses);
        assertTrue(licenses.hasContent());
        assertEquals(3, licenses.getItemsNumber());

        final Exception e = assertThrows(ServiceException.class, () -> {
            final BundleObtainParameters bundleObtainParameters2 = new BundleObtainParameters();
            bundleObtainParameters2.setLicenseeNumber("LE002-TEST");
            BundleService.obtain(context, "B001-TEST", bundleObtainParameters2);
        });
        assertEquals("NotFoundException: Requested licensee does not exist.", e.getMessage());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return BundleServiceResource.class;
    }

    @Path(REST_API_PATH + "/bundle")
    public static class BundleServiceResource extends AbstractNLICServiceResource {

        public BundleServiceResource() {
            super("bundle");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("ValidationException", "Bundle name is required.");
            }

            if (formParams.containsKey(Constants.PRICE) && !formParams.containsKey(Constants.CURRENCY)) {
                return errorResponse("IllegalArgumentException",
                        "'price' field must be accompanied with the 'currency' field");
            }
            if (formParams.containsKey(Constants.CURRENCY) && !formParams.containsKey(Constants.PRICE)) {
                return errorResponse("IllegalArgumentException",
                        "'currency' field can not be used without the 'price' field");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String number, final UriInfo uriInfo) {
            return delete(number, "B001-TEST", uriInfo.getQueryParameters());
        }

        @POST
        @Path("{number}/obtain")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response obtain(@PathParam("number") final String number,
                               final MultivaluedMap<String, String> formParams) {
            if (!"LE001-TEST".equals(formParams.getFirst(Constants.Licensee.LICENSEE_NUMBER))) {
                return errorResponse("NotFoundException", "Requested licensee does not exist.");
            }

            final String xmlResourcePath = String.format("%snetlicensing-bundle-obtain.xml", TEST_CASE_BASE);
            
            final Netlicensing netlicensing = JAXBUtils.readObject(xmlResourcePath, Netlicensing.class);
            return Response.ok(netlicensing).build();
        }
    }
}
