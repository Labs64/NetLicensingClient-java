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
