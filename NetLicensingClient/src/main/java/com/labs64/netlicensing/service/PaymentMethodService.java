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

/**
 * Provides payment method entity handling routines.
 */
public class PaymentMethodService {

    /**
     * Gets payment method by its number.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  the payment method number
     * @return the payment method
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static PaymentMethod get(final Context context, final String number) throws BaseCheckedException {
        return null;  // TODO: implement me...
    }

    /**
     * Returns payment methods of a vendor.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param filter  reserved for the future use, must be omitted / set to NULL
     * @return collection of payment method entities or null/empty list if nothing found.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static Page<PaymentMethod> list(final Context context, final String filter) throws BaseCheckedException {
        return null;  // TODO: implement me...
    }

    /**
     * Updates payment method properties.
     *
     * @param context             determines the vendor on whose behalf the call is performed
     * @param number              payment method number
     * @param updatePaymentMethod non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated PaymentMethod.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static PaymentMethod update(final Context context, final String number, final PaymentMethod updatePaymentMethod) throws BaseCheckedException {
        return null;  // TODO: implement me...
    }

}
