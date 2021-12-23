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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides product handling routines.
 * <p>
 * {@linkplain Product Product entity} represents the vendor product within NetLicensing. Usually it corresponds to an
 * actual product of the vendor, but variations possible - in some cases it may be feasible to configure two or more
 * separate products within NetLicensing for a single actual product of the vendor. Products comprise of multiple
 * {@linkplain ProductModuleService product modules}, and licensing rules are defined for each product module. The
 * products are completely independent of each other in terms of license management. Once product is configured,
 * {@linkplain LicenseeService licensees} can be registered with the product. Licensees can obtain
 * {@linkplain LicenseService licenses} according to configured {@linkplain LicenseTemplateService license templates}.
 */
public class ProductService {

    /**
     * Creates new product with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param product
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created product object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Product create(final Context context, final Product product) throws NetLicensingException {
        CheckUtils.paramNotNull(product, "product");

        return NetLicensingService.getInstance().post(context, Constants.Product.ENDPOINT_PATH, product.asRequestForm(), Product.class);
    }

    /**
     * Gets product by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the product number
     * @return the product
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Product get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, Constants.Product.ENDPOINT_PATH + "/" + number, null, Product.class);
    }

    /**
     * Returns products of a vendor.
     * 
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return collection of product entities or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Page<Product> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Product.ENDPOINT_PATH, params, Product.class);
    }

    /**
     * Updates product properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product number
     * @param product
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Product update(final Context context, final String number, final Product product)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(product, "product");

        return NetLicensingService.getInstance().post(context, Constants.Product.ENDPOINT_PATH + "/" + number, product.asRequestForm(),
                Product.class);
    }

    /**
     * Deletes product.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static void delete(final Context context, final String number, final boolean forceCascade)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.CASCADE, forceCascade);
        NetLicensingService.getInstance().delete(context, Constants.Product.ENDPOINT_PATH + "/" + number, params);
    }

}
