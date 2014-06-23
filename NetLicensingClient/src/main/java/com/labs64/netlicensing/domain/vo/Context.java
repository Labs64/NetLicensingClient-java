package com.labs64.netlicensing.domain.vo;

import com.labs64.netlicensing.domain.Constants;

/**
 * Provides calling context for the NetLicensing API calls.
 * <p/>
 * The Context object may differ depending on the level at which NetLicensing API is called.
 * <p/>
 * For the internal Java NetLicensing API the Context provides information about the targeted Vendor.
 */
public class Context extends GenericContext<String> {

    public Context setBaseUrl(final String baseUrl) {
        return (Context) this.setValue(Constants.BASE_URL, baseUrl);
    }

    public String getBaseUrl() {
        return getValue(Constants.BASE_URL);
    }

    public Context setUsername(final String username) {
        return (Context) this.setValue(Constants.USERNAME, username);
    }

    public String getUsername() {
        return getValue(Constants.USERNAME);
    }

    public Context setPassword(final String password) {
        return (Context) this.setValue(Constants.PASSWORD, password);
    }

    public String getPassword() {
        return getValue(Constants.PASSWORD);
    }

    public Context setApiKey(final String apiKey) {
        return (Context) this.setValue(Constants.Token.API_KEY, apiKey);
    }

    public String getApiKey() {
        return getValue(Constants.Token.API_KEY);
    }

    public Context setVendorNumber(final String vendorNumber) {
        return (Context) this.setValue(Constants.Vendor.VENDOR_NUMBER, vendorNumber);
    }

    public String getVendorNumber() {
        return getValue(Constants.Vendor.VENDOR_NUMBER);
    }

}
