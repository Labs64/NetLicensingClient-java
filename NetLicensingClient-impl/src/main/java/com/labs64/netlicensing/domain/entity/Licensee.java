package com.labs64.netlicensing.domain.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;

/**
 * Licensee entity used internally by NetLicensing.
 * <p>
 * Properties visible via NetLicensing API:
 * <p>
 * <b>number</b> - Unique number (across all products of a vendor) that identifies the licensee. Vendor can assign this
 * number when creating a licensee or let NetLicensing generate one. Read-only after creation of the first license for
 * the licensee.
 * <p>
 * <b>active</b> - If set to false, the licensee is disabled. Licensee can not obtain new licenses, and validation is
 * disabled (tbd).
 * <p>
 * Arbitrary additional user properties of string type may be associated with each licensee. The name of user property
 * must not be equal to any of the fixed property names listed above and must be none of <b>id, deleted,
 * productNumber</b>. See {@link PropertyBase} for details.
 */
public class Licensee extends BaseEntity {

    private Product product;

    private Collection<License> licenses;

    private Map<String, String> licenseeProperties;

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        product.getLicensees().add(this);
        this.product = product;
    }

    public Collection<License> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<>();
        }
        return licenses;
    }

    public void setLicenses(final Collection<License> licenses) {
        this.licenses = licenses;
    }

    public Map<String, String> getLicenseeProperties() {
        if (licenseeProperties == null) {
            licenseeProperties = new HashMap<String, String>();
        }
        return licenseeProperties;
    }

    public void setLicenseeProperties(final Map<String, String> licenseeProperties) {
        this.licenseeProperties = licenseeProperties;
    }

    public void addProperty(final String property, final String value) {
        getLicenseeProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getLicenseeProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.Product.PRODUCT_NUMBER); // maps to 'product'
        reserved.add(Constants.IN_USE);
        reserved.add(Constants.Vendor.VENDOR_NUMBER); // used by shop, therefore disallowed for user
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Licensee [");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
