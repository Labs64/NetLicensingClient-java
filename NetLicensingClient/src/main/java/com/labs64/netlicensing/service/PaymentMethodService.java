package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides payment method entity handling routines.
 */
public interface PaymentMethodService {

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
    PaymentMethod get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns payment methods of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return collection of payment method entities or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<PaymentMethod> list(Context context, String filter) throws BaseCheckedException;

    /**
     * Updates payment method properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            payment method number
     * @param updatePaymentMethod
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated PaymentMethod.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    PaymentMethod update(Context context, String number, PaymentMethod updatePaymentMethod) throws BaseCheckedException;

}
