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

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties;
import com.labs64.netlicensing.domain.vo.LicensingModelProperties;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.PageImpl;
import com.labs64.netlicensing.exception.BaseCheckedException;

/**
 * Provides utility routines.
 */
public class UtilityService {

    static final String CONTEXT_PATH = "utility";

    /**
     * Returns all license types.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @return collection of available license types or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.BaseCheckedException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.BaseCheckedException}. These exceptions
     *             will be transformed to the corresponding service response messages.
     */
    public static Page<String> listLicenseTypes(final Context context) throws BaseCheckedException {
        final Page<LicenseTypeProperties> licenseTypes = NetLicensingService.getInstance().list(context, CONTEXT_PATH + "/licenseTypes", null, LicenseTypeProperties.class);
        return new PageImpl<String>(
            (List<String>) CollectionUtils.collect(licenseTypes.getContent(), new Transformer<LicenseTypeProperties, String>() {

                @Override
                public String transform(LicenseTypeProperties licenseType) {
                    return licenseType.getName();
                }
            }),
            licenseTypes.getPageNumber(),
            licenseTypes.getItemsNumber(),
            licenseTypes.getTotalPages(),
            licenseTypes.getTotalItems(),
            licenseTypes.hasNext()
        );
    }

    /**
     * Returns all licensing models.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @return collection of available license models or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.BaseCheckedException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.BaseCheckedException}. These exceptions
     *             will be transformed to the corresponding service response messages.
     */
    public static Page<String> listLicensingModels(final Context context) throws BaseCheckedException {

        final Page<LicensingModelProperties> licensingModels = NetLicensingService.getInstance().list(context, CONTEXT_PATH + "/licensingModels", null, LicensingModelProperties.class);
        return new PageImpl<String>(
            (List<String>) CollectionUtils.collect(licensingModels.getContent(), new Transformer<LicensingModelProperties, String>() {

                @Override
                public String transform(LicensingModelProperties licensingModel) {
                    return licensingModel.getName();
                }
            }),
            licensingModels.getPageNumber(),
            licensingModels.getItemsNumber(),
            licensingModels.getTotalPages(),
            licensingModels.getTotalItems(),
            licensingModels.hasNext()
        );
    }

}
