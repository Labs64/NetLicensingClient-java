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

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Notification;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.util.CheckUtils;
import com.labs64.netlicensing.util.ConvertUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class NotificationService {
    /**
     * Creates new notification with given properties.
     *
     * @param context      determines the vendor on whose behalf the call is performed
     * @param notification non-null properties will be taken for the new notification, null properties will either stay null, or will
     *                     be set to a default value, depending on property.
     * @return the newly created notification object
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Notification create(final Context context, final Notification notification) throws NetLicensingException {
        CheckUtils.paramNotNull(notification, "notification");
        return NetLicensingService.getInstance().post(context, Constants.Notification.ENDPOINT_PATH, ConvertUtils.entityToForm(notification), Notification.class);
    }

    /**
     * Gets notification by its number.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  the notification number
     * @return the notification
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Notification get(final Context context, final String number) throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        return NetLicensingService.getInstance().get(context, Constants.Notification.ENDPOINT_PATH + "/" + number, null, Notification.class);
    }

    /**
     * Returns notifications of a vendor.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param filter  reserved for the future use, must be omitted / set to NULL
     * @return collection of notification entities or null/empty list if nothing found.
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Page<Notification> list(final Context context, final String filter) throws NetLicensingException {
        final Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(filter)) {
            params.put(Constants.FILTER, filter);
        }
        return NetLicensingService.getInstance().list(context, Constants.Notification.ENDPOINT_PATH, params, Notification.class);
    }

    /**
     * Updates notification properties.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  notification number
     * @param notification non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated notification.
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static Notification update(final Context context, final String number, final Notification notification)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");
        CheckUtils.paramNotNull(notification, "notification");

        return NetLicensingService.getInstance().post(context, Constants.Notification.ENDPOINT_PATH + "/" + number, ConvertUtils.entityToForm(notification),
                Notification.class);
    }

    /**
     * Deletes notification.
     *
     * @param context      determines the vendor on whose behalf the call is performed
     * @param number       notification number
     * @throws NetLicensingException any subclass of {@linkplain NetLicensingException}. These exceptions will be transformed to the
     *                               corresponding service response messages.
     */
    public static void delete(final Context context, final String number)
            throws NetLicensingException {
        CheckUtils.paramNotEmpty(number, "number");

        NetLicensingService.getInstance().delete(context, Constants.Notification.ENDPOINT_PATH + "/" + number, null);
    }

}
