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

import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides payment method entity handling routines.
 */
public class PaymentMethodService {

    static final String CONTEXT_PATH = "paymentmethod";

    /**
     * Gets payment method by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the payment method number
     * @return the payment method
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static PaymentMethod get(final Context context, final String number) throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, CONTEXT_PATH + "/" + number, null, PaymentMethod.class);
    }

    /**
     * Returns payment methods of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @return collection of payment method entities or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Page<PaymentMethod> list(final Context context) throws BaseCheckedException {
        return NetLicensingService.getInstance().list(context, CONTEXT_PATH, null, PaymentMethod.class);
    }

    /**
     * Updates payment method properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            payment method number
     * @param paymentMethod
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated PaymentMethod.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static PaymentMethod update(final Context context, final String number, final PaymentMethod paymentMethod)
            throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(paymentMethod, "paymentMethod");

        return NetLicensingService.getInstance().post(context, CONTEXT_PATH + "/" + number,
                paymentMethod.asRequestForm(), PaymentMethod.class);
    }

}
