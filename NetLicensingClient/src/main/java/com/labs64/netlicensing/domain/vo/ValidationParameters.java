package com.labs64.netlicensing.domain.vo;

import java.util.HashMap;
import java.util.Map;

public class ValidationParameters {

    private String productNumber;
    private String licenseeName;
    private String licenseeSecret;
    private String publicKey;
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

    /**
     * Sets the name for the new licensee
     * 
     * @param licenseeName
     *            optional human-readable licensee name in case licensee will be auto-created. This parameter must not
     *            be the name, but can be used to store any other useful string information with new licensees, up to
     *            1000 characters.
     */
    public void setLicenseeName(final String licenseeName) {
        this.licenseeName = licenseeName;
    }

    public String getLicenseeName() {
        return licenseeName;
    }

    /**
     * Sets the licensee secret
     * 
     * @param licenseeSecret
     *            licensee secret stored on the client side. Refer to Licensee Secret documentation for details.
     */
    public void setLicenseeSecret(final String licenseeSecret) {
        this.licenseeSecret = licenseeSecret;
    }

    public String getLicenseeSecret() {
        return licenseeSecret;
    }

    /**
     * Sets the public key
     *
     * @param publicKey
     *            publicKey stored on the client side.
     */
    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace(
                "-----END PUBLIC KEY-----", "");
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Map<String, Map<String, String>> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<String, Map<String, String>>();
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
