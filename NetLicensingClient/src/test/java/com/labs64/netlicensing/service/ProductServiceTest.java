package com.labs64.netlicensing.service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.TestProperties;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;
import com.labs64.netlicensing.util.JAXBUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */
public class ProductServiceTest extends BaseServiceTest {

    // *** NLIC test mock resource ***

    @Path(REST_API_PATH)
    public static class NLICResource {

        private ObjectFactory objectFactory = new ObjectFactory();

        @GET
        @Path("hello")
        public String getHello() {
            return "Hello NetLicensing!";
        }

        @GET
        @Path("product/{productNumber}")
        public Response getSession(@PathParam("productNumber") final String productNumber) {
            Netlicensing netlicensing = JAXBUtils.readObject(TEST_CASE_BASE + "netlicensing-product-get.xml", Netlicensing.class);

            SchemaFunction.propertyByName(netlicensing.getItems().getItem().iterator().next().getProperty(),
                    Constants.NUMBER).setValue(productNumber);

            return Response.ok(netlicensing).build();
        }

    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        return new ResourceConfig(NLICResource.class);
    }

    // *** NLIC Tests ***

    private static Context context;

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testHello() {
        final String hello = target(REST_API_PATH).path("hello").request().get(String.class);
        assertEquals("Hello NetLicensing!", hello);
    }

    @Test(expected = RestException.class)
    public void testNotExistingService() throws Exception {
        Context context = createContext();
        context.setBaseUrl("I_AM_NOT_EXISTING_SERVICE");
        ProductService.get(context, "dummyNumber");
    }

    @Ignore
    public void testCreate() throws Exception {

    }

    @Test
    public void testGet() throws Exception {
        final String number = RandomStringUtils.randomAlphanumeric(8);

        Product res = ProductService.get(context, number);

        assertNotNull(res);
        assertEquals(number, res.getNumber());
    }

    @Ignore
    public void testList() throws Exception {

    }

    @Ignore
    public void testUpdate() throws Exception {

    }

    @Ignore
    public void testDelete() throws Exception {

    }

}
