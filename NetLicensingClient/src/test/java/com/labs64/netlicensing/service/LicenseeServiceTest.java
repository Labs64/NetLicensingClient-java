package com.labs64.netlicensing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;

/**
 * Integration tests for {@link LicenseeService}.
 */
public class LicenseeServiceTest extends BaseServiceTest {

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

    }
}
