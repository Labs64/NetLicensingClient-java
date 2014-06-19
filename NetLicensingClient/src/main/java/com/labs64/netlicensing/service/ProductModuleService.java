package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides product module handling routines.
 * <p>
 * {@linkplain ProductService Product} may comprise of multiple product modules, but must have at least one. Each
 * product module is licensed using one of the licensing models offered by NetLicensing service. Licensing within a
 * product module is independent of other product modules, however all product modules of a single product are visible
 * to every {@linkplain LicenseeService licensee} of the product.
 */
public interface ProductModuleService {

    /**
     * Creates new product module object with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productNumber
     *            parent product to which the new product module is to be added
     * @param newProductModule
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created product module object
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    ProductModule create(Context context, String productNumber, ProductModule newProductModule)
            throws BaseCheckedException;

    /**
     * Gets product module by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the product module number
     * @return the product module
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    ProductModule get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns all product modules of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return list of product modules (of all products) or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<ProductModule> list(Context context, String filter) throws BaseCheckedException;

    /**
     * Updates product module properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product module number
     * @param updateProductModule
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product module.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    ProductModule update(Context context, String number, ProductModule updateProductModule)
            throws BaseCheckedException;

    /**
     * Deletes product module.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            product module number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    void delete(Context context, String number, boolean forceCascade) throws BaseCheckedException;
}
