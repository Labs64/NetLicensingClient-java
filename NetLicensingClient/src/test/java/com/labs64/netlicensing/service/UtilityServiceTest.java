/**
 *
 */
package com.labs64.netlicensing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Integration tests for {@link UtilityService}.
 */
public class UtilityServiceTest extends BaseServiceTest {

    // *** NLIC Tests ***

    private static Context context;

    @BeforeClass
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

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return UtilityServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + UtilityService.CONTEXT_PATH)
    public static class UtilityServiceResource {

        @GET
        @Path("licensingModels")
        public Response list() {
            final String xmlResourcePath = String.format("%snetlicensing-licensingModels-list.xml", TEST_CASE_BASE);
            final Netlicensing netlicensing = JAXBUtils.readObject(xmlResourcePath, Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

    }

}
