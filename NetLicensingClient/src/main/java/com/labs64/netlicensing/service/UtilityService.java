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

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.TokenType;
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
    public static Page<String> listLicenseTypes(final Context context, final TokenType tokenType)
            throws BaseCheckedException {
        final Map<String, Object> params = new HashMap<String, Object>();

        // TODO: ...

        return NetLicensingService.getInstance().list(context, CONTEXT_PATH, params, String.class);
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
    public static Page<String> listLicensingModels(final Context context, final TokenType tokenType)
            throws BaseCheckedException {
        final Map<String, Object> params = new HashMap<String, Object>();

        // TODO: ...

        return NetLicensingService.getInstance().list(context, CONTEXT_PATH, params, String.class);
    }

}
