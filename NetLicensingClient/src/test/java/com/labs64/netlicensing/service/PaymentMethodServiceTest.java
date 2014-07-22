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

import javax.ws.rs.Path;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Integration tests for {@link PaymentMethodService}.
 */
public class PaymentMethodServiceTest extends BaseServiceTest {

    private static final String PAYMENT_METHOD_CUSTOM_PROPERTY = "paypal.subject";

    // *** NLIC Tests ***

    private static Context context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testGet() throws Exception {
        final PaymentMethod paymentMethod = PaymentMethodService.get(context, "PAYPAL");

        assertNotNull(paymentMethod);
        assertEquals("PAYPAL", paymentMethod.getNumber());
        assertEquals(true, paymentMethod.getActive());
        assertEquals("sample_paypal_subject", paymentMethod.getPaymentMethodProperties().get(PAYMENT_METHOD_CUSTOM_PROPERTY));
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return PaymentMethodServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + PaymentMethodService.CONTEXT_PATH)
    public static class PaymentMethodServiceResource extends AbstractNLICServiceResource {

        public PaymentMethodServiceResource() {
            super("paymentMethod");
        }

    }

}
