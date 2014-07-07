package com.labs64.netlicensing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.TestProperties;
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
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;
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
        thrown.expect(RestException.class);
        thrown.expectMessage("MalformedRequestException: Product number is not provided");

        final Licensee newLicensee = new LicenseeImpl();
        LicenseeService.create(context, null, newLicensee);
    }

    @Test
    public void testCreateEmptyWithProductNumber() throws Exception {
        final Licensee newLicensee = new LicenseeImpl();
        final Licensee createdLicensee = LicenseeService.create(context, "P001-TEST", newLicensee);

        assertNotNull(createdLicensee);
        assertEquals(true, createdLicensee.getActive());
        assertEquals("P001-TEST", createdLicensee.getProduct().getNumber());
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
        final List<Licensee> licensees = LicenseeService.list(context, null);

        assertNotNull(licensees);
        assertEquals(3, licensees.size());
        assertEquals("L001-TEST", licensees.get(0).getNumber());
        assertEquals(true, licensees.get(1).getActive());
        assertEquals("P001-TEST", licensees.get(2).getProduct().getNumber());
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
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        return new ResourceConfig(NLICResource.class);
    }

    @Path(REST_API_PATH)
    public static class NLICResource {

        private final ObjectFactory objectFactory = new ObjectFactory();

        @Path("licensee")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response createLicensee(final MultivaluedMap<String, String> formParams) {

            final Netlicensing netlicensing = objectFactory.createNetlicensing();

            if (!formParams.containsKey(Constants.Product.PRODUCT_NUMBER)) {
                SchemaFunction.setSingleInfo(netlicensing, "MalformedRequestException", InfoEnum.ERROR,
                        "Product number is not provided");
                return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
            }

            netlicensing.setItems(objectFactory.createNetlicensingItems());
            netlicensing.getItems().getItem().add(objectFactory.createItem());

            final Map<String, String> propertyValues = new HashMap<String, String>();
            // default values
            propertyValues.put(Constants.ACTIVE, "true");
            // values from request
            for (final String paramKey : formParams.keySet()) {
                propertyValues.put(paramKey, formParams.getFirst(paramKey));
            }
            SchemaFunction.updateProperties(netlicensing.getItems().getItem().get(0).getProperty(), propertyValues);

            return Response.ok(netlicensing).build();
        }

        @Path("licensee/{licenseeNumber}")
        @GET
        public Response getLicensee(@PathParam("licenseeNumber") final String licenseeNumber) {
            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE
                    + "netlicensing-licensee-get.xml", Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

        @Path("licensee")
        @GET
        public Response listLicensees() {
            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE + "netlicensing-licensee-list.xml",
                    Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

        @Path("licensee/{licenseeNumber}")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response updateLicensee(@PathParam("licenseeNumber") final String licenseeNumber,
                final MultivaluedMap<String, String> formParams) {

            final Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE + "netlicensing-licensee-update.xml",
                    Netlicensing.class);

            final Map<String, String> propertyValues = new HashMap<String, String>();
            for (final String paramKey : formParams.keySet()) {
                propertyValues.put(paramKey, formParams.getFirst(paramKey));
            }
            SchemaFunction.updateProperties(netlicensing.getItems().getItem().get(0).getProperty(), propertyValues);

            return Response.ok(netlicensing).build();
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
