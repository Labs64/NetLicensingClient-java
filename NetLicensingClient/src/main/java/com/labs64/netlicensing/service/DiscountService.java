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

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.exception.ServiceException;
import com.labs64.netlicensing.provider.Form;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides discount handling routines.
 */
public class DiscountService {

    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final String NOT_FOUND_EXCEPTION_PREFIX = "NotFoundException:";

    /**
     * Resolves a discount license template by product number and promo code.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productNumber
     *            product number that defines promo code uniqueness scope
     * @param promoCode
     *            promo code to resolve
     * @return resolved discount license template or null if discount was not found
     * @throws NetLicensingException
     *             any non-not-found service error
     */
    public static LicenseTemplate resolve(final Context context, final String productNumber, final String promoCode)
            throws NetLicensingException {
        return resolve(context, productNumber, promoCode, null);
    }

    /**
     * Resolves a discount license template by product number and promo code.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productNumber
     *            product number that defines promo code uniqueness scope
     * @param promoCode
     *            promo code to resolve
     * @param cartTotal
     *            optional cart total used by the API to validate minimum cart total
     * @return resolved discount license template or null if discount was not found
     * @throws NetLicensingException
     *             any non-not-found service error
     */
    public static LicenseTemplate resolve(final Context context, final String productNumber, final String promoCode,
            final BigDecimal cartTotal) throws NetLicensingException {
        CheckUtils.paramNotEmpty(productNumber, "productNumber");
        CheckUtils.paramNotEmpty(promoCode, "promoCode");

        final Form form = new Form();
        if (cartTotal != null) {
            form.param(Constants.Product.Discount.CART_TOTAL, cartTotal.toString());
        }

        try {
            return NetLicensingService.getInstance().post(context, resolveEndpoint(productNumber, promoCode), form,
                    LicenseTemplate.class);
        } catch (final ServiceException e) {
            if (isNotFound(e)) {
                return null;
            }
            throw e;
        }
    }

    private static String resolveEndpoint(final String productNumber, final String promoCode) {
        return Constants.Product.ENDPOINT_PATH + "/" + productNumber + "/"
                + Constants.Product.Discount.ENDPOINT_PATH + "/" + promoCode + "/"
                + Constants.Product.Discount.ENDPOINT_RESOLVE_PATH;
    }

    private static boolean isNotFound(final ServiceException e) {
        return (e.getStatusCode() == HTTP_STATUS_NOT_FOUND)
                || StringUtils.startsWith(e.getMessage(), NOT_FOUND_EXCEPTION_PREFIX);
    }
}
