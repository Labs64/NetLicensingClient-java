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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.entity.impl.TransactionImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.TransactionSource;
import com.labs64.netlicensing.domain.vo.TransactionStatus;
import com.labs64.netlicensing.exception.RestException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Integration tests for {@link TransactionService}.
 */
public class TransactionServiceTest extends BaseServiceTest {

    private static final String TRANSACTION_CUSTOM_PROPERTY = "customProperty";
    private static final String TRANSACTION_DELETING_PROPERTY = "toBeDeleted";

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
        final Transaction newTransaction = new TransactionImpl();
        newTransaction.setSource(TransactionSource.AUTO_LICENSE_CREATE);
        newTransaction.setStatus(TransactionStatus.CLOSED);
        newTransaction.setNumber("TR001TEST");
        newTransaction.setActive(false);
        newTransaction.setCurrency(Currency.EUR);
        newTransaction.setPrice(new BigDecimal("100"));
        newTransaction.setDiscount(new BigDecimal("5"));
        newTransaction.addProperty(TRANSACTION_CUSTOM_PROPERTY, "Custom property value");

        final Transaction createdTransaction = TransactionService.create(context, newTransaction);
        assertNotNull(createdTransaction);
        assertEquals(TransactionSource.AUTO_LICENSE_CREATE, createdTransaction.getSource());
        assertEquals(TransactionStatus.CLOSED, createdTransaction.getStatus());
        assertEquals("TR001TEST", createdTransaction.getNumber());
        assertEquals(false, createdTransaction.getActive());
        assertEquals(Currency.EUR, createdTransaction.getCurrency());
        assertEquals(new BigDecimal("100.00"), createdTransaction.getPrice());
        assertEquals(new BigDecimal("5.00"), createdTransaction.getDiscount());
        assertEquals("Custom property value",
                createdTransaction.getTransactionProperties().get(TRANSACTION_CUSTOM_PROPERTY));
    }

    @Test
    public void testCreateEmpty() throws Exception {
        thrown.expect(RestException.class);

        final Transaction newTransaction = new TransactionImpl();
        TransactionService.create(context, newTransaction);
    }

    @Test
    public void testCreateWithRequiredPropertiesOnly() throws Exception {
        final Transaction newTransaction = new TransactionImpl();
        newTransaction.setSource(TransactionSource.SHOP);
        newTransaction.setStatus(TransactionStatus.PENDING);

        final Transaction createdTransaction = TransactionService.create(context, newTransaction);
        assertNotNull(createdTransaction);
        assertEquals(true, createdTransaction.getActive());
    }

    @Test
    public void testCreateWithPriceAndDiscountAndWithoutCurrency() throws Exception {
        final Transaction newTransaction = new TransactionImpl();
        newTransaction.setSource(TransactionSource.SHOP);
        newTransaction.setStatus(TransactionStatus.PENDING);
        newTransaction.setPrice(new BigDecimal("80"));
        newTransaction.setDiscount(new BigDecimal("4"));

        thrown.expect(RestException.class);
        TransactionService.create(context, newTransaction);
    }

    @Test
    public void testCreateWithPriceAndCurrencyAndWithoutDiscount() throws Exception {
        final Transaction newTransaction = new TransactionImpl();
        newTransaction.setSource(TransactionSource.SHOP);
        newTransaction.setStatus(TransactionStatus.PENDING);
        newTransaction.setPrice(new BigDecimal("80"));
        newTransaction.setCurrency(Currency.EUR);

        thrown.expect(RestException.class);
        TransactionService.create(context, newTransaction);
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
        assertEquals(Currency.EUR, transaction.getCurrency());
        assertEquals(DateUtils.parseDate("2014-07-07T21:30:46.658Z").getTime(), transaction.getDateCreated());
        assertEquals(DateUtils.parseDate("2014-07-07T21:30:46.658Z").getTime(), transaction.getDateClosed());
        assertEquals("VTEST", transaction.getTransactionProperties().get("vendorNumber"));
    }

    @Test
    public void testList() throws Exception {
        final Page<Transaction> transactions = TransactionService.list(context, null);

        assertNotNull(transactions);
        assertTrue(transactions.hasContent());
        assertEquals(3, transactions.getItemsNumber());
        assertEquals(TransactionSource.AUTO_LICENSE_UPDATE, transactions.getContent().get(0).getSource());
        assertEquals(TransactionStatus.PENDING, transactions.getContent().get(1).getStatus());
        assertEquals("TR003TEST", transactions.getContent().get(2).getNumber());
    }

    @Test
    public void testUpdate() throws Exception {
        final Transaction transaction = new TransactionImpl();
        transaction.setNumber("TR002TEST");
        transaction.setSource(TransactionSource.SHOP);
        transaction.setStatus(TransactionStatus.CLOSED);
        transaction.addProperty(TRANSACTION_CUSTOM_PROPERTY, "New property value");
        transaction.addProperty(TRANSACTION_DELETING_PROPERTY, "");

        final Transaction createdTransaction = TransactionService.update(context, "TR001TEST", transaction);
        assertNotNull(createdTransaction);
        assertEquals("TR002TEST", createdTransaction.getNumber());
        assertEquals(TransactionSource.AUTO_LICENSE_CREATE, createdTransaction.getSource());
        assertEquals(TransactionStatus.CLOSED, createdTransaction.getStatus());
        assertEquals(true, createdTransaction.getActive());
        assertEquals("New property value",
                createdTransaction.getTransactionProperties().get(TRANSACTION_CUSTOM_PROPERTY));
        assertNull(createdTransaction.getTransactionProperties().get(TRANSACTION_DELETING_PROPERTY));
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return TransactionServiceResource.class;
    }

    @Path(REST_API_PATH + "/" + Constants.Transaction.ENDPOINT_PATH)
    public static class TransactionServiceResource extends AbstractNLICServiceResource {

        public TransactionServiceResource() {
            super("transaction");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            if (!formParams.containsKey(Constants.Transaction.SOURCE)
                    || !formParams.containsKey(Constants.Transaction.STATUS)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
            }
            if (formParams.containsKey(Constants.PRICE)
                    && (!formParams.containsKey(Constants.DISCOUNT) || !formParams.containsKey(Constants.CURRENCY))) {
                return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
            }

            roundParamValueToTwoDecimalPlaces(formParams, Constants.PRICE);
            roundParamValueToTwoDecimalPlaces(formParams, Constants.DISCOUNT);

            final Map<String, String> defaultPropertyValues = new HashMap<String, String>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response update(final String number, final MultivaluedMap<String, String> formParams) {
            // property "Source" cannot be updated
            formParams.remove(Constants.Transaction.SOURCE);
            return super.update(number, formParams);
        }

    }

}
