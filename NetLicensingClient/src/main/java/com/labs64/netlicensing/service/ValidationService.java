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

import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Form;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.MetaInfo;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.CheckUtils;
import com.labs64.netlicensing.util.SignatureUtils;

import org.apache.commons.lang3.StringUtils;

public class ValidationService {

    public static ValidationResult validate(final Context context, final String number,
            final ValidationParameters validationParameters, final MetaInfo... meta) throws NetLicensingException {
        return convertValidationResult(receiveValidationFile(context, number, validationParameters), meta);
    }

    public static Netlicensing receiveValidationFile(final Context context, final String number,
            final ValidationParameters validationParameters) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        Form form = convertValidationParameters(validationParameters);
        final NetLicensingService service = NetLicensingService.getInstance();
        return service.request(context, HttpMethod.POST,
                Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_VALIDATE, form,
                null);
    }

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
            if (StringUtils.isNotBlank(validationParameters.getLicenseeName())) {
                form.param(Constants.Licensee.PROP_LICENSEE_NAME, validationParameters.getLicenseeName());
            }
            if (StringUtils.isNotBlank(validationParameters.getLicenseeSecret())) {
                form.param(Constants.Licensee.PROP_LICENSEE_SECRET, validationParameters.getLicenseeSecret());
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