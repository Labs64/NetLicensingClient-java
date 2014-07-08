package com.labs64.netlicensing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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
        assertEquals(3, licensees.getContent().size());
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

        final Licensee updatedLicensee = LicenseeService.update(context, "L001-TEST", licensee);

        assertNotNull(updatedLicensee);
        assertEquals("L002-TEST", updatedLicensee.getNumber());
        assertEquals(true, updatedLicensee.getActive());
        assertEquals("P001-TEST", updatedLicensee.getProduct().getNumber());
        assertEquals("New property value", updatedLicensee.getLicenseeProperties().get(LICENSEE_CUSTOM_PROPERTY));
    }

    @Test
    public void testDelete() throws Exception {
        LicenseeService.delete(context, "L001-TEST", true);

        thrown.expect(RestException.class);
        thrown.expectMessage("NotFoundException: requested licensee does not exist");
        LicenseeService.delete(context, "L001-NONE", false);
    }

    @Test
    public void testValidate() throws Exception {
        final ValidationResult result = LicenseeService.validate(context, "L001-TEST", null);

        assertNotNull(result);

        final Composition validation = result.getProductModuleValidation("M001-TEST");
        assertNotNull(validation);
        assertEquals("TimeLimitedEvaluation", validation.getProperties().get(Constants.ProductModule.LICENSING_MODEL)
                .getValue());
        assertEquals("Test module", validation.getProperties().get(Constants.ProductModule.PRODUCT_MODULE_NAME)
                .getValue());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return NLICResource.class;
    }

    @Path(REST_API_PATH)
    public static class NLICResource extends AbstractNLICServiceResource {

        public NLICResource() {
            super("licensee");
        }

        @Path("licensee")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response createLicensee(final MultivaluedMap<String, String> formParams) {

            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                return errorResponse("MalformedRequestException", "Product number is not provided");
            }

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Path("licensee/{licenseeNumber}")
        @GET
        public Response getLicensee(@PathParam("licenseeNumber") final String licenseeNumber) {
            return get();
        }

        @Path("licensee")
        @GET
        public Response listLicensees() {
            return list();
        }

        @Path("licensee/{licenseeNumber}")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response updateLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                final MultivaluedMap<String, String> formParams) {
            return update(formParams);
        }

        @Path("licensee/{licenseeNumber}")
        @DELETE
        public Response deleteLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                @QueryParam("forceCascade") final boolean forceCascade) {
            return delete(licenseeNumber, "L001-TEST", forceCascade);
        }

        @Path("licensee/{licenseeNumber}/validate")
        @GET
        public Response validateLicensee(@PathParam("licenseeNumber") final String licenseeNumber) {
            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE
                    + "netlicensing-licensee-validate.xml", Netlicensing.class);
            return Response.ok(netlicensing).build();
        }
    }
}
