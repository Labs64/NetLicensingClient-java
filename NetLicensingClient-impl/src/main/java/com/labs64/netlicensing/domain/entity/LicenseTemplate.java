package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;

/**
 * License template entity used internally by NetLicensing.
 * <p>
 * Properties visible via NetLicensing API:
 * <p>
 * <b>number</b> - Unique number (across all products of a vendor) that identifies the license template. Vendor can
 * assign this number when creating a license template or let NetLicensing generate one. Read-only after creation of the
 * first license from this license template.
 * <p>
 * <b>active</b> - If set to false, the license template is disabled. Licensee can not obtain any new licenses off this
 * license template.
 * <p>
 * <b>name</b> - Name for the licensed item.
 * <p>
 * <b>licenseType</b> - type of licenses created from this license template. Supported types: "FEATURE", "TIMEVOLUME".
 * <p>
 * <b>price</b> - price for the license. If >0, it must always be accompanied by the currency specification.
 * <p>
 * <b>currency</b> - specifies currency for the license price. Check data types to discover which currencies are
 * supported.
 * <p>
 * <b>automatic</b> - If set to true, every new licensee automatically gets one license out of this license template on
 * creation. Automatic licenses must have their price set to 0.
 * <p>
 * <b>hidden</b> - If set to true, this license template is not shown in NetLicensing Shop as offered for purchase.
 * <p>
 * <b>hideLicenses</b> - If set to true, licenses from this license template are not visible to the end customer, but
 * participate in validation.
 * <p>
 */
public class LicenseTemplate extends BaseEntity {

    private ProductModule productModule;

    private String name;

    private String licenseType;

    private BigDecimal price;

    private String currency;

    private Boolean automatic = Boolean.FALSE;

    private Boolean hidden = Boolean.FALSE;

    private Boolean hideLicenses = Boolean.FALSE;

    private Collection<License> licenses;

    private Map<String, String> licenseTemplateProperties;

    public ProductModule getProductModule() {
        return productModule;
    }

    public void setProductModule(final ProductModule productModule) {
        productModule.getLicenseTemplates().add(this);
        this.productModule = productModule;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(final String licenseType) {
        this.licenseType = licenseType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(final Boolean automatic) {
        this.automatic = automatic;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(final Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getHideLicenses() {
        return hideLicenses;
    }

    public void setHideLicenses(final Boolean hideLicenses) {
        this.hideLicenses = hideLicenses;
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

    public Map<String, String> getLicenseTemplateProperties() {
        if (licenseTemplateProperties == null) {
            licenseTemplateProperties = new HashMap<String, String>();
        }
        return licenseTemplateProperties;
    }

    public void setLicenseTemplateProperties(final Map<String, String> licenseTemplateProperties) {
        this.licenseTemplateProperties = licenseTemplateProperties;
    }

    public void addProperty(final String property, final String value) {
        getLicenseTemplateProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getLicenseTemplateProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.ProductModule.PRODUCT_MODULE_NUMBER); // maps to 'productModule'
        reserved.add(Constants.ProductModule.PRODUCT_MODULE_NAME); // maps to 'productModule'
        reserved.add(Constants.NAME);
        reserved.add(Constants.LicenseTemplate.LICENSE_TYPE);
        reserved.add(Constants.PRICE);
        reserved.add(Constants.CURRENCY);
        reserved.add(Constants.IN_USE);
        reserved.add(Constants.LicenseTemplate.AUTOMATIC);
        reserved.add(Constants.LicenseTemplate.HIDDEN);
        reserved.add(Constants.LicenseTemplate.HIDE_LICENSES);
        reserved.add(Constants.Shop.PROP_SHOP_LICENSE_ID); // used by shop in licenses, therefore disallowed for user
        reserved.add(Constants.Shop.PROP_SHOPPING_CART); // used by shop in licenses, therefore disallowed for user
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(LicenseTemplate.class.getSimpleName());
        builder.append(" [");
        builder.append(super.toString());
        builder.append(", ");
        builder.append(Constants.NAME);
        builder.append("=");
        builder.append(getName());
        builder.append(", ");
        builder.append(Constants.LicenseTemplate.LICENSE_TYPE);
        builder.append("=");
        builder.append(getLicenseType());
        builder.append(", ");
        builder.append(Constants.PRICE);
        builder.append("=");
        builder.append(getPrice());
        builder.append(", ");
        builder.append(Constants.CURRENCY);
        builder.append("=");
        builder.append(getCurrency());
        builder.append(", ");
        builder.append(Constants.LicenseTemplate.AUTOMATIC);
        builder.append("=");
        builder.append(getAutomatic());
        builder.append(", ");
        builder.append(Constants.LicenseTemplate.HIDDEN);
        builder.append("=");
        builder.append(getHidden());
        builder.append(", ");
        builder.append(Constants.LicenseTemplate.HIDE_LICENSES);
        builder.append("=");
        builder.append(getHideLicenses());
        builder.append("]");
        return builder.toString();
    }

}
