package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides product handling routines.
 * <p>
 * {@linkplain Product Product entity} represents the vendor product within NetLicensing. Usually it corresponds to an
 * actual product of the vendor, but variations possible - in some cases it may be feasible to configure two or more
 * separate products within NetLicensing for a single actual product of the vendor. Products comprise of multiple
 * {@linkplain ProductModuleService product modules}, and licensing rules are defined for each product module. The
 * products are completely independent of each other in terms of license management. Once product is configured,
 * {@linkplain LicenseeService licensees} can be registered with the product. Licensees can obtain
 * {@linkplain LicenseService licenses} according to configured {@linkplain LicenseTemplateService license templates}.
 */
public interface ProductService {

    /**
     * Creates new product with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param newProduct
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created product object
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Product create(Context context, Product newProduct) throws BaseCheckedException;

    /**
     * Gets product by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the product number
     * @return the product
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Product get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns products of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return collection of product entities or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<Product> list(Context context, String filter) throws BaseCheckedException;

    /**
     * Updates product properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product number
     * @param updateProduct
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Product update(Context context, String number, Product updateProduct) throws BaseCheckedException;

    /**
     * Deletes product.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    void delete(Context context, String number, boolean forceCascade) throws BaseCheckedException;

}
