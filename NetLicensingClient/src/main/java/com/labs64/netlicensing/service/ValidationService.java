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

import java.util.Map;
import java.util.Map.Entry;

import com.labs64.netlicensing.provider.HttpMethod;
import com.labs64.netlicensing.provider.Form;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.MetaInfo;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.CheckUtils;
import com.labs64.netlicensing.util.SignatureUtils;

/**
 * Provides routines for validating the licenses.
 */
public class ValidationService {

    /**
     * Validates active licenses of the licensee.
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
        return convertValidationResult(retrieveValidationFile(context, number, validationParameters), meta);
    }

    /**
     * Retrieves validation file for the given licensee from the server as {@link Netlicensing} object. The file can be
     * stored locally for subsequent validation by {@link #validateOffline} method, that doesn't require connection to
     * the server.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param validationParameters
     *            optional validation parameters. See ValidationParameters and licensing model documentation for
     *            details.
     * @return validation file, possibly signed, for subsequent use in {@link #validateOffline}
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static Netlicensing retrieveValidationFile(final Context context, final String number,
            final ValidationParameters validationParameters) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        final Form form = convertValidationParameters(validationParameters);
        final NetLicensingService service = NetLicensingService.getInstance();
        return service.request(context, HttpMethod.POST,
                Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_VALIDATE, form,
                null);
    }

    /**
     * Perform validation without connecting to the server (offline) using validation file previously retrieved by
     * {@link #retrieveValidationFile}.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param validationFile
     *            validation file returned by {@link #retrieveValidationFile} call
     * @return result of the validation
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    public static ValidationResult validateOffline(final Context context, final Netlicensing validationFile,
            final MetaInfo... meta) throws NetLicensingException {
        SignatureUtils.check(context, validationFile);
        return convertValidationResult(validationFile, meta);
    }

    private static Form convertValidationParameters(final ValidationParameters validationParameters) {
        final Form form = new Form();
        if (validationParameters != null) {
            if (StringUtils.isNotBlank(validationParameters.getProductNumber())) {
                form.param(Constants.Product.PRODUCT_NUMBER, validationParameters.getProductNumber());
            }

            validationParameters.getLicenseeProperties()
                    .forEach((k, v) -> {
                        if (StringUtils.isNotBlank(k)) {
                            form.param(k, v);
                        }
                    });

            if (Boolean.TRUE.equals(validationParameters.isDryRun())) {
                form.param(Constants.Validation.DRY_RUN, Boolean.TRUE.toString());
            }
            if (Boolean.TRUE.equals(validationParameters.isForOfflineUse())) {
                form.param(Constants.Validation.FOR_OFFLINE_USE, Boolean.TRUE.toString());
            }
            int pmIndex = 0;
            for (final Entry<String, Map<String, String>> productModuleValidationParams : validationParameters
                    .getParameters().entrySet()) {
                form.param(Constants.ProductModule.PRODUCT_MODULE_NUMBER.concat(Integer.toString(pmIndex)),
                        productModuleValidationParams.getKey());
                for (final Entry<String, String> param : productModuleValidationParams.getValue().entrySet()) {
                    form.param(param.getKey().concat(Integer.toString(pmIndex)), param.getValue());
                }
                ++pmIndex;
            }
        }
        return form;
    }

    private static ValidationResult convertValidationResult(final Netlicensing validationFile, final MetaInfo... meta)
            throws NetLicensingException {
        if (validationFile == null) {
            return null;
        } else {
            final NetLicensingService service = NetLicensingService.getInstance();
            return service.processResponse(meta, validationFile, ValidationResult.class);
        }
    }
}