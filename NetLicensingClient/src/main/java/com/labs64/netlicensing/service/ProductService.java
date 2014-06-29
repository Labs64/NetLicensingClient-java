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

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Form;

import com.labs64.netlicensing.domain.EntityFactory;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.provider.RestProviderJersey;
import com.labs64.netlicensing.schema.context.Netlicensing;

/**
 * Provides product handling routines.
 * <p/>
 * {@linkplain Product Product entity} represents the vendor product within
 * NetLicensing. Usually it corresponds to an actual product of the vendor, but
 * variations possible - in some cases it may be feasible to configure two or
 * more separate products within NetLicensing for a single actual product of the
 * vendor. Products comprise of multiple {@linkplain ProductModuleService
 * product modules}, and licensing rules are defined for each product module.
 * The products are completely independent of each other in terms of license
 * management. Once product is configured, {@linkplain LicenseeService
 * licensees} can be registered with the product. Licensees can obtain
 * {@linkplain LicenseService licenses} according to configured
 * {@linkplain LicenseTemplateService license templates}.
 */
public class ProductService {

    /**
     * Creates new product with given properties.
     *
     * @param context    determines the vendor on whose behalf the call is performed
     * @param newProduct non-null properties will be taken for the new object, null
     *                   properties will either stay null, or will be set to a default
     *                   value, depending on property.
     * @return the newly created product object
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These
     *                              exceptions will be transformed to the corresponding service
     *                              response messages.
     */
    public static Product create(final Context context, final Product newProduct) throws BaseCheckedException {

        final Netlicensing res = RestProviderJersey.getInstance().call(context, HttpMethod.POST, "product",
                getProductAsRequest(newProduct), Netlicensing.class, null);
        return EntityFactory.create(res.getItems().getItem().get(0), Product.class);
    }

    /**
     * Gets product by its number.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  the product number
     * @return the product
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These
     *                              exceptions will be transformed to the corresponding service
     *                              response messages.
     */
    public static Product get(final Context context, final String number) throws BaseCheckedException {

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("productNumber", number);
        final Netlicensing res = RestProviderJersey.getInstance().call(context, HttpMethod.GET,
                "product/{productNumber}", null, Netlicensing.class, params);

        // TODO: find&use only suitable for this context item
        return EntityFactory.create(res.getItems().getItem().get(0), Product.class);
    }

    /**
     * Returns products of a vendor.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param filter  reserved for the future use, must be omitted / set to NULL
     * @return collection of product entities or null/empty list if nothing
     *         found.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These
     *                              exceptions will be transformed to the corresponding service
     *                              response messages.
     */
    public static Page<Product> list(final Context context, final String filter) throws BaseCheckedException {
        return null; // TODO: implement me...
    }

    /**
     * Updates product properties.
     *
     * @param context       determines the vendor on whose behalf the call is performed
     * @param number        product number
     * @param updateProduct non-null properties will be updated to the provided values,
     *                      null properties will stay unchanged.
     * @return updated product.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These
     *                              exceptions will be transformed to the corresponding service
     *                              response messages.
     */
    public static Product update(final Context context, final String number, final Product updateProduct) throws BaseCheckedException {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("productNumber", number);

        final Netlicensing res = RestProviderJersey.getInstance().call(context, HttpMethod.POST,
                "product/{productNumber}", getProductAsRequest(updateProduct), Netlicensing.class, params);

        // TODO: find&use only suitable for this context item
        return EntityFactory.create(res.getItems().getItem().get(0), Product.class);
    }

    /**
     * Deletes product.
     *
     * @param context      determines the vendor on whose behalf the call is performed
     * @param number       product number
     * @param forceCascade if true, any entities that depend on the one being deleted
     *                     will be deleted too
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These
     *                              exceptions will be transformed to the corresponding service
     *                              response messages.
     */
    public static void delete(final Context context, final String number, final boolean forceCascade) throws BaseCheckedException {
        // TODO: implement me...
    }

    private static Form getProductAsRequest(final Product product) {
        final Form form = new Form();
        if (product.getNumber() != null) {
            form.param("number", product.getNumber());
        }
        if (product.getActive() != null) {
            form.param("active", product.getActive().toString());
        }
        if (product.getName() != null) {
            form.param("name", product.getName());
        }
        if (product.getVersion() != null) {
            form.param("version", product.getVersion());
        }
        if (product.getLicenseeAutoCreate() != null) {
            form.param("licenseeAutoCreate", product.getLicenseeAutoCreate().toString());
        }
        for (String propKey : product.getProductProperties().keySet()) {
            final String propValue = product.getProductProperties().get(propKey);
            if (propValue != null) {
                form.param(propKey, propValue);
            }
        }
        return form;
    }

}
