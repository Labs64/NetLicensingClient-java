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
package com.labs64.netlicensing.domain.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * This enum defines available payment methods.
 */
public enum PaymentMethodEnum {

    /**
     * "null" payment method is a placeholder for undefined/unset value.
     */
    NULL,

    DIRECT_DEBIT,

    PAYPAL,

    PAYPAL_SANDBOX,

    CREDIT_CARD,

    GOOGLE_CHECKOUT,

    CASH_ON_DELIVERY,

    PAYMENT_ON_INVOICE,

    FINANCING;

    public static PaymentMethodEnum parseString(final String paymentMethod) {
        if (StringUtils.isBlank(paymentMethod)) {
            return null;
        }

        try {
            return PaymentMethodEnum.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
