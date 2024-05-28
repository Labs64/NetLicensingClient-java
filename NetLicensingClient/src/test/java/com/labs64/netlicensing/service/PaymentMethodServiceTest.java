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

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.impl.PaymentMethodImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link PaymentMethodService}.
 */
public class PaymentMethodServiceTest extends BaseServiceTest {

    private static final String PAYMENT_METHOD_CUSTOM_PROPERTY = "paypal.subject";
    private static final String PAYMENT_METHOD_DELETING_PROPERTY = "toBeDeleted";

    // *** NLIC Tests ***

    private static Context context;

    @BeforeAll
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testGet() throws Exception {
        final PaymentMethod paymentMethod = PaymentMethodService.get(context, "PAYPAL");

        assertNotNull(paymentMethod);
        assertEquals("PAYPAL", paymentMethod.getNumber());
        assertEquals(true, paymentMethod.getActive());
        assertEquals("sample_paypal_subject", paymentMethod.getProperties().get(PAYMENT_METHOD_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<PaymentMethod> paymentMethods = PaymentMethodService.list(context, null);

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
                updatedPaymentMethod.getProperties().get(PAYMENT_METHOD_CUSTOM_PROPERTY));
        assertNull(updatedPaymentMethod.getProperties().get(PAYMENT_METHOD_DELETING_PROPERTY));
    }

    @Test
    public void testUpdateWithNumber() throws Exception {
        final PaymentMethod paymentMethod = new PaymentMethodImpl();
        paymentMethod.setNumber("PAYPAL_NEW");

        final Exception e = assertThrows(ServiceException.class, () -> {
            PaymentMethodService.update(context, "PAYPAL", paymentMethod);
        });
        assertEquals("MalformedRequestException: Requested payment method is not supported", e.getMessage());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return PaymentMethodServiceResource.class;
    }

    @Path(REST_API_PATH + "/paymentmethod")
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
