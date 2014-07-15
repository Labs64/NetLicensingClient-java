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

import java.math.BigDecimal;

import javax.ws.rs.Path;

import org.junit.BeforeClass;
import org.junit.Test;

import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.TransactionSource;
import com.labs64.netlicensing.domain.vo.TransactionStatus;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Integration tests for {@link TransactionService}.
 */
public class TransactionServiceTest extends BaseServiceTest {

    // *** NLIC Tests ***

    private static Context context;

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testGet() throws Exception {
        final Transaction transaction = TransactionService.get(context, "TR001TEST");

        assertNotNull(transaction);
        assertEquals("TR001TEST", transaction.getNumber());
        assertEquals(true, transaction.getActive());
        assertEquals(TransactionStatus.CLOSED, transaction.getStatus());
        assertEquals(TransactionSource.SHOP, transaction.getSource());
        assertEquals(new BigDecimal("21.00"), transaction.getPrice());
        assertEquals(new BigDecimal("9.00"), transaction.getDiscount());
        assertEquals(Currency.EUR.value(), transaction.getCurrency());
        assertEquals(DateUtils.parseDate("2014-07-07T21:30:46.658Z").getTime(), transaction.getDateCreated());
        assertEquals(DateUtils.parseDate("2014-07-07T21:30:46.658Z").getTime(), transaction.getDateClosed());
        assertEquals("VTEST", transaction.getTransactionProperties().get("vendorNumber"));
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return TransactionServiceResource.class;
    }

    @Path(REST_API_PATH + "/transaction")
    public static class TransactionServiceResource extends AbstractNLICServiceResource {

        public TransactionServiceResource() {
            super("transaction");
        }

    }

}
