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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides transaction handling routines.
 * <p/>
 * Transaction is created each time change to {@linkplain LicenseService licenses} happens. For instance licenses are
 * obtained by a licensee, licenses disabled by vendor, licenses deleted, etc. Transaction is created no matter what
 * source has initiated the change to licenses: it can be either a direct purchase of licenses by a licensee via
 * NetLicensing Shop, or licenses can be given to a licensee by a vendor. Licenses can also be assigned implicitly by
 * NetLicensing if it is defined so by a license model (e.g. evaluation license may be given automatically). All these
 * events are reflected in transactions. Of all the transaction handling routines only read-only routines are exposed to
 * the public API, as transactions are only allowed to be created and modified by NetLicensing internally.
 */
public class TransactionService {

    /**
     * Creates new transaction object with given properties.
     * <p/>
     * This routine is for internal use by NetLicensing. Where appropriate, transactions will be created by NetLicensing
     * automatically.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param transaction
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created transaction object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Transaction create(final Context context, final Transaction transaction) throws NetLicensingException {
        CheckUtils.paramNotNull(transaction, "transaction");

        return NetLicensingService.getInstance().post(context, Constants.Transaction.ENDPOINT_PATH,
                transaction.asRequestForm(),
                Transaction.class);
    }

    /**
     * Gets transaction by its number.
     * <p/>
     * Use this operation for getting details about certain transaction. List of all transactions can be obtained by the
     * {@link #list(Context, String)} operation.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the transaction number
     * @return the transaction
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Transaction get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, Constants.Transaction.ENDPOINT_PATH + "/" + number, null,
                Transaction.class);
    }

    /**
     * Returns all transactions of a vendor.
     * <p/>
     * Use this operation to get the list of all transactions.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return list of transactions (of all products/licensees) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Page<Transaction> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Transaction.ENDPOINT_PATH, params,
                Transaction.class);
    }

    /**
     * Updates transaction properties.
     * <p/>
     * This routine is for internal use by NetLicensing. Where appropriate, transactions will be modified by
     * NetLicensing automatically.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            transaction number
     * @param transaction
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated transaction.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Transaction update(final Context context, final String number, final Transaction transaction)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(transaction, "transaction");

        return NetLicensingService.getInstance().post(context, Constants.Transaction.ENDPOINT_PATH + "/" + number,
                transaction.asRequestForm(), Transaction.class);
    }

}
