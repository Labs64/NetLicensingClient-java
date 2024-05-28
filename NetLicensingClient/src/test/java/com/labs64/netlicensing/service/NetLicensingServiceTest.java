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

import jakarta.ws.rs.GET;
import com.labs64.netlicensing.provider.HttpMethod;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.context.ObjectFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Common integration tests for {@link NetLicensingService}.
 */
public class NetLicensingServiceTest extends BaseServiceTest {

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testNotExistingService() throws Exception {
        assertThrows(RestException.class, () -> {
            NetLicensingService.getInstance().request(context, HttpMethod.GET, "non-existing-service", null, null);            
        });
    }

    @Test
    public void testUnsupportedStatusCode() throws Exception {
        assertThrows(RestException.class, () -> {
            NetLicensingService.getInstance().request(context, HttpMethod.GET, "unsupported-status-code", null, null);
        });
    }

    // *** NLIC test mock resource ***

    @Override
    protected java.lang.Class<?> getResourceClass() {
        return NLICResource.class;
    }

    @Path(REST_API_PATH)
    public static class NLICResource {

        private final ObjectFactory objectFactory = new ObjectFactory();

        @Path("unsupported-status-code")
        @GET
        public Response getUnsupportedStatusCode() {
            return Response.status(Response.Status.PARTIAL_CONTENT)
                    .entity(objectFactory.createNetlicensing())
                    .build();
        }
    }

}
