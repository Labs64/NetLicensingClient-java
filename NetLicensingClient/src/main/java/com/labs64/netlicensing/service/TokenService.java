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
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;
import com.labs64.netlicensing.util.ConvertUtils;

/**
 * Provides token entity handling routines.
 */
public class TokenService {
    
    /**
     * Gets token by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the token number
     * @return the token
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Token get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        return NetLicensingService.getInstance().get(context, Constants.Token.ENDPOINT_PATH + "/" + number, null, Token.class);
    }

    /**
     * Returns tokens of a vendor.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            additional criteria to filter type of tokens to return, if NULL return tokens of all types
     * @return collection of token entities or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Page<Token> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Token.ENDPOINT_PATH, params, Token.class);
    }

    /**
     * Creates new token.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param token
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return created token
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static Token create(final Context context, final Token token) throws NetLicensingException {
        CheckUtils.paramNotNull(token, "token");

        return NetLicensingService.getInstance().post(context, Constants.Token.ENDPOINT_PATH, ConvertUtils.entityToForm(token), Token.class);
    }

    /**
     * Delete token by its number.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the token number
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     *             any subclass of {@linkplain com.labs64.netlicensing.exception.NetLicensingException}. These
     *             exceptions will be transformed to the corresponding service response messages.
     */
    public static void delete(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        NetLicensingService.getInstance().delete(context, Constants.Token.ENDPOINT_PATH + "/" + number, null);
    }

}
