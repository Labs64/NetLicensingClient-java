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
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;

/**
 * Provides license template handling routines.
 * <p/>
 * License template is a configuration element of a {@linkplain ProductModuleService product module}. License templates
 * define concrete items available for a {@linkplain LicenseeService licensee} to obtain. License template specifies
 * what is an item, its price, amount (if applicable), etc. When licensee obtains an item, actual
 * {@linkplain LicenseService license} is created off the corresponding license template, and this license is assigned
 * to the licensee. Thus, the item is licensed for the Licensee.
 */
public class LicenseTemplateService {

    static final String CONTEXT_PATH = "licensetemplate";

    /**
     * Creates new license template object with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productModuleNumber
     *            parent product module to which the new license template is to be added
     * @param licenseTemplate
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created license template object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static LicenseTemplate create(final Context context, final String productModuleNumber,
            final LicenseTemplate licenseTemplate) throws NetLicensingException {
        CheckUtils.paramNotNull(licenseTemplate, "licenseTemplate");

        final Form form = licenseTemplate.asRequestForm();
        if (!StringUtils.isEmpty(productModuleNumber)) {
            form.param(Constants.ProductModule.PRODUCT_MODULE_NUMBER, productModuleNumber);
        }
        return NetLicensingService.getInstance().post(context, CONTEXT_PATH, form, LicenseTemplate.class);
    }

    /**
     * Gets license template by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the license template number
     * @return the license template
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static LicenseTemplate get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, CONTEXT_PATH + "/" + number, null, LicenseTemplate.class);
    }

    /**
     * Returns all license templates of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @return list of license templates (of all products/modules) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static Page<LicenseTemplate> list(final Context context) throws NetLicensingException {
        return NetLicensingService.getInstance().list(context, CONTEXT_PATH, null, LicenseTemplate.class);
    }

    /**
     * Updates license template properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license template number
     * @param licenseTemplate
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated license template.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    public static LicenseTemplate update(final Context context, final String number,
            final LicenseTemplate licenseTemplate) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(licenseTemplate, "licenseTemplate");

        return NetLicensingService.getInstance().post(context, CONTEXT_PATH + "/" + number,
                licenseTemplate.asRequestForm(), LicenseTemplate.class);
    }

    /**
     * Deletes license template.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license template number
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
        NetLicensingService.getInstance().delete(context, CONTEXT_PATH + "/" + number, params);
    }

}
