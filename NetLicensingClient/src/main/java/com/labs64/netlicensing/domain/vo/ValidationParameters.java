package com.labs64.netlicensing.domain.vo;

import java.util.HashMap;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;

public class ValidationParameters {

    private String productNumber;
    private Map<String, String> licenseeProperties;
    private Boolean dryRun;
    private Boolean forOfflineUse;
    private Map<String, Map<String, String>> parameters;

    /**
     * Sets the target product
     *
     * @param productNumber
     *            optional productNumber, must be provided in case licensee auto-create is enabled
     */
    public void setProductNumber(final String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public Map<String, String> getLicenseeProperties() {
        if (licenseeProperties == null) {
            licenseeProperties = new HashMap<>();
        }
        return licenseeProperties;
    }

    public void setLicenseeProperty(final String key, final String value) {
        getLicenseeProperties().put(key, value);
    }


    /**
     * Sets the name for the new licensee
     *
     * @param licenseeName
     *            optional human-readable licensee name in case licensee will be auto-created. This parameter must not
     *            be the name, but can be used to store any other useful string information with new licensees, up to
     *            1000 characters.
     */
    public void setLicenseeName(final String licenseeName) {
        setLicenseeProperty(Constants.Licensee.PROP_LICENSEE_NAME, licenseeName);
    }

    public String getLicenseeName() {
        return getLicenseeProperties().get(Constants.Licensee.PROP_LICENSEE_NAME);
    }

    /**
     * Sets the licensee secret
     *
     * @param licenseeSecret
     *            licensee secret stored on the client side. Refer to Licensee Secret documentation for details.
     */
    public void setLicenseeSecret(final String licenseeSecret) {
        setLicenseeProperty(Constants.Licensee.PROP_LICENSEE_SECRET, licenseeSecret);
    }

    public String getLicenseeSecret() {
        return getLicenseeProperties().get(Constants.Licensee.PROP_LICENSEE_SECRET);
    }


    /**
     * Sets the "dry run" mode
     *
     * @param dryRun
     *            if "true", validation will be executed in "dry run" mode, i.e. no modifications to any licenses.
     */
    public void setDryRun(final Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public Boolean isDryRun() {
        return dryRun;
    }

    /**
     * Indicates, that the validation response is intended the offline use
     *
     * @param forOfflineUse
     *            if "true", validation response will be extended with data required for the offline use
     */
    public void setForOfflineUse(final Boolean forOfflineUse) {
        this.forOfflineUse = forOfflineUse;
    }

    public Boolean isForOfflineUse() {
        return forOfflineUse;
    }

    public Map<String, Map<String, String>> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        return parameters;
    }

    public Map<String, String> getProductModuleValidationParameters(final String productModuleNumber) {
        if (!getParameters().containsKey(productModuleNumber)) {
            getParameters().put(productModuleNumber, new HashMap<String, String>());
        }
        return getParameters().get(productModuleNumber);
    }

    public void setProductModuleValidationParameters(final String productModuleNumber,
            final Map<String, String> productModuleParameters) {
        getParameters().put(productModuleNumber, productModuleParameters);
    }

    public void put(final String productModuleNumber, final String key, final String value) {
        getProductModuleValidationParameters(productModuleNumber).put(key, value);
    }

}
