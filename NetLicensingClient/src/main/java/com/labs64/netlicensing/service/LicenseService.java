package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides license handling routines.
 * <p>
 * Licenses always belong to a certain {@linkplain LicenseeService licensee}. Licenses are cloned off the corresponding
 * {@linkplain LicenseTemplateService license templates} when a licensee obtains them. On validation request the
 * licenses that belong to a licensee are processed according to the licensing models configured for the product
 * modules, and the validation result is sent back for further processing to the requesting side (which is typically a
 * vendor service or a program executed by the end user).
 * <p>
 * In simple cases there is no need to work with licenses directly via NetLicensing API, as licenses are assigned either
 * by a licensing model automatically (e.g. evaluation licenses) or actively purchased by a licensee via NetLicensing
 * Shop. However, for some complex licensing models, there may be a need to assign licenses to a licensee
 * programmatically from the vendor software.
 */
public interface LicenseService {

    /**
     * Creates new license object with given properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param licenseeNumber
     *            parent licensee to which the new license is to be added
     * @param licenseTemplateNumber
     *            license template that the license is created from
     * @param transactionNumber
     *            For privileged logins specifies transaction for the license creation. For regular logins new
     *            transaction always created implicitly, and the operation will be in a separate transaction.
     *            Transaction is generated with the provided transactionNumber, or, if transactionNumber is null, with
     *            auto-generated number.
     * @param newLicense
     *            non-null properties will be taken for the new object, null properties will either stay null, or will
     *            be set to a default value, depending on property.
     * @return the newly created license object
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    License create(Context context, String licenseeNumber, String licenseTemplateNumber,
            String transactionNumber, License newLicense) throws BaseCheckedException;

    /**
     * Gets license by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the license number
     * @return the license
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    License get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns licenses of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return list of licenses (of all products) or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<License> list(Context context, String filter) throws BaseCheckedException;

    /**
     * Updates license properties.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license number
     * @param transactionNumber
     *            transaction for the license update. Created implicitly if transactionNumber is null. In this case the
     *            operation will be in a separate transaction.
     * @param updateLicense
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated license.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    License update(Context context, String number, String transactionNumber, License updateLicense)
            throws BaseCheckedException;

    /**
     * Deletes license.
     * <p>
     * When any license is deleted, corresponding transaction is created automatically.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            license number
     * @param forceCascade
     *            if true, any entities that depend on the one being deleted will be deleted too
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    void delete(Context context, String number, boolean forceCascade) throws BaseCheckedException;
}
