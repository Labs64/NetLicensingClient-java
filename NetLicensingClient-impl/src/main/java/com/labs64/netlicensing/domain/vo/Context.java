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

    private boolean delegated = false;

    public Context setVendorNumber(final String vendorNumber) {
        return (Context) this.setValue(Constants.Vendor.VENDOR_NUMBER, vendorNumber);
    }

    public String getVendorNumber() {
        return getValue(Constants.Vendor.VENDOR_NUMBER);
    }

    public Context setDelegated(final boolean delegated) {
        this.delegated = delegated;
        return this;
    }

    public boolean isDelegated() {
        return delegated;
    }

}
