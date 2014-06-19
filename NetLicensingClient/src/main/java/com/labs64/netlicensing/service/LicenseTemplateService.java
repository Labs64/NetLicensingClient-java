package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides license template handling routines.
 * <p>
 * License template is a configuration element of a {@linkplain ProductModuleService product module}. License templates
 * define concrete items available for a {@linkplain LicenseeService licensee} to obtain. License template specifies
 * what is an item, its price, amount (if applicable), etc. When licensee obtains an item, actual
 * {@linkplain LicenseService license} is created off the corresponding license template, and this license is assigned
 * to the licensee. Thus, the item is licensed for the Licensee.
 */
public interface LicenseTemplateService {

    /**
     * Creates new license template object with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param productModuleNumber
     *            parent product module to which the new license template is to be added
     * @param newLicenseTemplate
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created license template object
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    LicenseTemplate create(Context context, String productModuleNumber, LicenseTemplate newLicenseTemplate)
            throws BaseCheckedException;

    /**
     * gets license template by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the license template number
     * @return the license template
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    LicenseTemplate get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns all license templates of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return list of license templates (of all products/modules) or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<LicenseTemplate> list(Context context, String filter) throws BaseCheckedException;

    /**
     * Updates license template properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license template number
     * @param updateLicenseTemplate
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated license template.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    LicenseTemplate update(Context context, String number, LicenseTemplate updateLicenseTemplate)
            throws BaseCheckedException;

    /**
     * Deletes license template.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license template number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    void delete(Context context, String number, boolean forceCascade) throws BaseCheckedException;
}
