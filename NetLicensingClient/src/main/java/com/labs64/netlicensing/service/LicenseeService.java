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
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.MetaInfo;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides licensee handling routines.
 * <p>
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
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static Licensee create(final Context context, final String productNumber, final Licensee licensee)
            throws NetLicensingException {
        CheckUtils.paramNotNull(licensee, "licensee");

        final Form form = licensee.asRequestForm();
        if (StringUtils.isNotBlank(productNumber)) {
            form.param(Constants.Product.PRODUCT_NUMBER, productNumber);
        }
        return NetLicensingService.getInstance().post(context, Constants.Licensee.ENDPOINT_PATH, form, Licensee.class);
    }

    /**
     * Gets licensee by its number.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the licensee number
     * @return the licensee
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static Licensee get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, null, Licensee.class);
    }

    /**
     * Returns all licensees of a vendor.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return list of licensees (of all products) or null/empty list if nothing found.
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static Page<Licensee> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Licensee.ENDPOINT_PATH, params, Licensee.class);
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
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static Licensee update(final Context context, final String number, final Licensee licensee)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(licensee, "licensee");

        return NetLicensingService.getInstance().post(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, licensee.asRequestForm(),
                Licensee.class);
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
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static void delete(final Context context, final String number, final boolean forceCascade)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        final Map<String, Object> params = new HashMap<>();
        params.put(Constants.CASCADE, forceCascade);
        NetLicensingService.getInstance().delete(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, params);
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
     * @param validationParameters
     *            optional validation parameters, specific to licensing model. See licensing model documentation for
     *            details.
     * @param meta
     *            optional parameter, receiving messages returned within response <infos> section.
     * @return result of the validation
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    @Deprecated
    public static ValidationResult validate(final Context context, final String number, final String productNumber,
            final String licenseeName, final ValidationParameters validationParameters, final MetaInfo... meta)
                    throws NetLicensingException {
        validationParameters.setProductNumber(productNumber);
        validationParameters.setLicenseeName(licenseeName);
        return validate(context, number, validationParameters, meta);
    }

    /**
     * Validates active licenses of the licensee.
     * In the case of multiple product modules validation,
     * required parameters indexes will be added automatically.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param validationParameters
     *            optional validation parameters. See ValidationParameters and licensing model documentation for
     *            details.
     * @param meta
     *            optional parameter, receiving messages returned within response <infos> section.
     * @return result of the validation
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static ValidationResult validate(final Context context, final String number,
            final ValidationParameters validationParameters, final MetaInfo... meta) throws NetLicensingException {
        return ValidationService.validate(context, number, validationParameters, meta);
    }

    /**
     * Transfer licenses between licensees.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the number of the licensee receiving licenses
     * @param sourceLicenseeNumber
     *            the number of the licensee delivering licenses
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static void transfer(final Context context, final String number, final String sourceLicenseeNumber)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotEmpty(sourceLicenseeNumber, Constants.Licensee.SOURCE_LICENSEE_NUMBER);

        final Form form = new Form();
        form.param(Constants.Licensee.SOURCE_LICENSEE_NUMBER, sourceLicenseeNumber);

        NetLicensingService.getInstance().post(context,
                Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_TRANSFER, form,
                Licensee.class);
    }

}
