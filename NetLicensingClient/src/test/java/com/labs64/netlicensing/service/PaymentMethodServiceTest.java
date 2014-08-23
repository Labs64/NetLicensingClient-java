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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.impl.PaymentMethodImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;

/**
 * Integration tests for {@link PaymentMethodService}.
 */
public class PaymentMethodServiceTest extends BaseServiceTest {

    private static final String PAYMENT_METHOD_CUSTOM_PROPERTY = "paypal.subject";
    private static final String PAYMENT_METHOD_DELETING_PROPERTY = "toBeDeleted";

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
        assertEquals("sample_paypal_subject",
                paymentMethod.getPaymentMethodProperties().get(PAYMENT_METHOD_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<PaymentMethod> paymentMethods = PaymentMethodService.list(context);

        assertNotNull(paymentMethods);
        assertTrue(paymentMethods.hasContent());
        assertEquals(2, paymentMethods.getContent().size());
        assertEquals("PAYPAL_SANDBOX", paymentMethods.getContent().get(0).getNumber());
        assertEquals("PAYPAL", paymentMethods.getContent().get(1).getNumber());
        assertEquals(true, paymentMethods.getContent().get(1).getActive());
    }

    @Test
    public void testUpdate() throws Exception {
        final PaymentMethod paymentMethod = new PaymentMethodImpl();
        paymentMethod.setActive(false);
        paymentMethod.addProperty(PAYMENT_METHOD_CUSTOM_PROPERTY, "new_sample_paypal_subject");
        paymentMethod.addProperty(PAYMENT_METHOD_DELETING_PROPERTY, "");

        final PaymentMethod updatedPaymentMethod = PaymentMethodService.update(context, "PAYPAL", paymentMethod);

        assertNotNull(updatedPaymentMethod);
        assertEquals(false, updatedPaymentMethod.getActive());
        assertEquals("new_sample_paypal_subject",
                updatedPaymentMethod.getPaymentMethodProperties().get(PAYMENT_METHOD_CUSTOM_PROPERTY));
        assertNull(updatedPaymentMethod.getPaymentMethodProperties().get(PAYMENT_METHOD_DELETING_PROPERTY));
    }

    @Test
    public void testUpdateWithNumber() throws Exception {
        final PaymentMethod paymentMethod = new PaymentMethodImpl();
        paymentMethod.setNumber("PAYPAL_NEW");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("MalformedRequestException: Requested payment method is not supported");
        PaymentMethodService.update(context, "PAYPAL", paymentMethod);
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

        @Override
        public Response update(final String number, final MultivaluedMap<String, String> formParams) {
            if (formParams.containsKey(Constants.NUMBER) && !number.equals(formParams.getFirst(Constants.NUMBER))) {
                return errorResponse("MalformedRequestException", "Requested payment method is not supported");
            }

            return super.update(number, formParams);
        }

    }

}
