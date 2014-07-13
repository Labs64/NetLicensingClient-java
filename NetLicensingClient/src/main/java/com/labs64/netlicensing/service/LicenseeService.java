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

import javax.ws.rs.core.Form;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.ValidationResult;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides licensee handling routines.
 * <p/>
 * Licensee is usually an end customer, capable of obtaining licenses for the product. Each licensee is associated with
 * a single {@linkplain ProductService product}. From the vendor perspective a licensee object in NetLicensing may
 * correspond to a physical instance of the product, customer account within a vendor's service, or it can represent a
 * security dongle given to a user. In practice, each licensee must only have a unique identifier associated with it,
 * that is communicated to NetLicensing as licensee number for performing operations related to this licensee. Licensee
 * doesn't need to have an own account within NetLicensing. There are two main operations performed for licensees:
 * validation and obtaining new licenses. Validation process is typically completely transparent to the end user and
 * performed from the vendor's product by means of this API. Licensee can be offered to obtain new licenses for the
 * product by redirecting the user to the NetLicensing Shop in the web browser.
 */
public class LicenseeService {

    /**
     * Creates new licensee object with given properties.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productNumber
     *            parent product to which the new licensee is to be added
     * @param licensee
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created licensee object
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Licensee create(final Context context, final String productNumber, final Licensee licensee) throws BaseCheckedException {
        CheckUtils.paramNotNull(licensee, "licensee");

        final Form form = licensee.asRequestForm();
        if (!StringUtils.isEmpty(productNumber)) {
            form.param(Constants.Product.PRODUCT_NUMBER, productNumber);
        }
        return NetLicensingService.post(context, "licensee", form, Licensee.class);
    }

    /**
     * Gets licensee by its number.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the licensee number
     * @return the licensee
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Licensee get(final Context context, final String number) throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.get(context, "licensee/" + number, null, Licensee.class);
    }

    /**
     * Returns all licensees of a vendor.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @return list of licensees (of all products) or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Page<Licensee> list(final Context context) throws BaseCheckedException {
        return NetLicensingService.list(context, "licensee", Licensee.class);
    }

    /**
     * Updates licensee properties.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param licensee
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated licensee.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Licensee update(final Context context, final String number, final Licensee licensee) throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(licensee, "licensee");

        return NetLicensingService.post(context, "licensee/" + number, licensee.asRequestForm(), Licensee.class);
    }

    /**
     * Deletes licensee.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static void delete(final Context context, final String number, final boolean forceCascade) throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.CASCADE, forceCascade);
        NetLicensingService.delete(context, "licensee/" + number, params);
    }

    /**
     * Validates active licenses of the licensee.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param productNumber
     *            optional productNumber, must be provided in case licensee auto-create is enabled
     * @param licenseeName
     *            optional human-readable licensee name in case licensee will be auto-created
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static ValidationResult validate(final Context context, final String number, final String productNumber, final String licenseeName) throws BaseCheckedException {
        CheckUtils.paramNotEmpty(number, "number");

        final Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(productNumber)) {
            params.put(Constants.Product.PRODUCT_NUMBER, productNumber);
        }
        if (!StringUtils.isEmpty(licenseeName)) {
            params.put(Constants.Licensee.PROP_NAME, licenseeName);
        }
        return NetLicensingService.get(context, "licensee/" + number + "/validate", params, ValidationResult.class);
    }

}
