package com.labs64.netlicensing.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Form;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.provider.RestProvider;
import com.labs64.netlicensing.provider.RestProviderJersey;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.converter.ItemToProductConverter;

/**
 * Provides product handling routines.
 * <p/>
 * {@linkplain Product Product entity} represents the vendor product within NetLicensing. Usually it corresponds to an
 * actual product of the vendor, but variations possible - in some cases it may be feasible to configure two or more
 * separate products within NetLicensing for a single actual product of the vendor. Products comprise of multiple
 * {@linkplain ProductModuleService product modules}, and licensing rules are defined for each product module. The
 * products are completely independent of each other in terms of license management. Once product is configured,
 * {@linkplain LicenseeService licensees} can be registered with the product. Licensees can obtain
 * {@linkplain LicenseService licenses} according to configured {@linkplain LicenseTemplateService license templates}.
 */
public class ProductService {

    /**
     * Creates new product with given properties.
     *
     * @param context    determines the vendor on whose behalf the call is performed
     * @param newProduct non-null properties will be taken for the new object, null properties will either stay null, or will
     *                   be set to a default value, depending on property.
     * @return the newly created product object
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static Product create(Context context, Product newProduct) throws BaseCheckedException {
        final RestProvider restProvider = new RestProviderJersey(context.getBaseUrl());
        restProvider.authenticate(context.getUsername(), context.getPassword());

        final Form form = new Form()
            .param("number", newProduct.getNumber())
            .param("active", newProduct.getActive().toString())
            .param("name", newProduct.getName())
            .param("version", newProduct.getVersion())
            .param("licenseeAutoCreate", newProduct.getLicenseeAutoCreate().toString());
        for (String propKey : newProduct.getProductProperties().keySet()) {
            form.param(propKey, newProduct.getProductProperties().get(propKey));
        }

        final Netlicensing res = restProvider.call(HttpMethod.POST, "product", form, Netlicensing.class, null);

        final Converter<Item, Product> converter = new ItemToProductConverter();
        return converter.convert(res.getItems().getItem().get(0));
    }

    /**
     * Gets product by its number.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param number  the product number
     * @return the product
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static Product get(Context context, String number) throws BaseCheckedException {
        // TODO: externalize RestProvider creation; be aware of different auth methods: user/pass or token
        RestProvider restProvider = new RestProviderJersey(context.getBaseUrl());
        restProvider.authenticate(context.getUsername(), context.getPassword());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productNumber", number);
        Netlicensing res = restProvider.call(HttpMethod.GET, "product/{productNumber}", null, Netlicensing.class, params);

        // TODO: generalize convertors usage via factory class
        Converter<Item, Product> converter = new ItemToProductConverter();

        // TODO: find&use only suitable for this context item
        return converter.convert(res.getItems().getItem().get(0));
    }

    /**
     * Returns products of a vendor.
     *
     * @param context determines the vendor on whose behalf the call is performed
     * @param filter  reserved for the future use, must be omitted / set to NULL
     * @return collection of product entities or null/empty list if nothing found.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static Page<Product> list(Context context, String filter) throws BaseCheckedException {
        return null;  // TODO: implement me...
    }

    /**
     * Updates product properties.
     *
     * @param context       determines the vendor on whose behalf the call is performed
     * @param number        product number
     * @param updateProduct non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product.
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static Product update(Context context, String number, Product updateProduct) throws BaseCheckedException {
        return null;  // TODO: implement me...
    }

    /**
     * Deletes product.
     *
     * @param context      determines the vendor on whose behalf the call is performed
     * @param number       product number
     * @param forceCascade if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *                              corresponding service response messages.
     */
    public static void delete(Context context, String number, boolean forceCascade) throws BaseCheckedException {
        // TODO: implement me...
    }

}
