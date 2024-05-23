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

import com.labs64.netlicensing.provider.HttpMethod;
import com.labs64.netlicensing.provider.Form;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.EntityFactory;
import com.labs64.netlicensing.domain.entity.Bundle;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.util.CheckUtils;
import com.labs64.netlicensing.util.ConvertUtils;

public class BundleService {
    private static final EntityFactory entityFactory = new EntityFactory();

    /**
     * Creates new bundle with given properties.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param bundle  non-null properties will be taken for the new bundle, null properties will either stay null, or will
     *                be set to a default value, depending on property.
     * @return the newly created bundle object
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Bundle create(final Context context, final Bundle bundle) throws NetLicensingException {
        CheckUtils.paramNotNull(bundle, "bundle");
        return NetLicensingService.getInstance().post(context, Constants.Bundle.ENDPOINT_PATH, ConvertUtils.entityToForm(bundle), Bundle.class);
    }

    /**
     * Gets bundle by its number.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  the bundle number
     * @return the bundle
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Bundle get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        return NetLicensingService.getInstance().get(context, Constants.Bundle.ENDPOINT_PATH + "/" + number, null, Bundle.class);
    }

    /**
     * Returns bundles of a vendor.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param filter  reserved for the future use, must be omitted / set to NULL
     * @return collection of bundle entities or null/empty list if nothing found.
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Page<Bundle> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Bundle.ENDPOINT_PATH, params, Bundle.class);
    }

    /**
     * Updates bundle properties.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  notification number
     * @param bundle  non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated bundle.
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Bundle update(final Context context, final String number, final Bundle bundle)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(bundle, "bundle");

        return NetLicensingService.getInstance().post(context, Constants.Bundle.ENDPOINT_PATH + "/" + number, ConvertUtils.entityToForm(bundle),
                Bundle.class);
    }

    /**
     * Deletes bundle.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  bundle number
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static void delete(final Context context, final String number)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        NetLicensingService.getInstance().delete(context, Constants.Bundle.ENDPOINT_PATH + "/" + number, null);
    }

    /**
     * Obtain bundle(create licenses from a bundle license templates).
     *
     * @param context                determines the vendor on whose behalf the call is performed
     * @param number                 bundle number
     * @param licenseeNumber         licensee number
     * @return collection of created licenses.
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Page<License> obtain(final Context context, final String number, final String licenseeNumber)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotEmpty(licenseeNumber, "licenseeNumber");

        final String endpoint = Constants.Bundle.ENDPOINT_PATH + "/" + number + "/" + Constants.Bundle.ENDPOINT_OBTAIN_PATH;

        final Form form = new Form();
        form.param(Constants.Licensee.LICENSEE_NUMBER, licenseeNumber);

        final Netlicensing response = NetLicensingService.getInstance().request(context, HttpMethod.POST, endpoint, form, null);

        return entityFactory.createPage(response, License.class);
    }
}
