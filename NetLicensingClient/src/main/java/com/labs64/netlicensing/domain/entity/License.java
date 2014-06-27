/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;

/**
 * License entity used internally by NetLicensing.
 * <p/>
 * Properties visible via NetLicensing API:
 * <p/>
 * <b>number</b> - Unique number (across all products/licensees of a vendor) that identifies the license. Vendor can
 * assign this number when creating a license or let NetLicensing generate one. Read-only after corresponding creation
 * transaction status is set to closed.
 * <p/>
 * <b>name</b> - Name for the licensed item. Set from license template on creation, if not specified explicitly.
 * <p/>
 * <b>active</b> - If set to false, the license is disabled. License can be re-enabled, but as long as it is disabled,
 * the license is excluded from the validation process.
 * <p/>
 * <b>price</b> - price for the license. If >0, it must always be accompanied by the currency specification. Read-only,
 * set from license template on creation.
 * <p/>
 * <b>currency</b> - specifies currency for the license price. Check data types to discover which currencies are
 * supported. Read-only, set from license template on creation.
 * <p/>
 * <b>hidden</b> - If set to true, this license is not shown in NetLicensing Shop as purchased license. Set from license
 * template on creation, if not specified explicitly.
 * <p/>
 * Arbitrary additional user properties of string type may be associated with each license. The name of user property
 * must not be equal to any of the fixed property names listed above and must be none of <b>id, deleted, licenseeNumber,
 * licenseTemplateNumber</b>. See {@link PropertyBase} for details.
 */
public class License extends BaseEntity {

    private String name;

    private BigDecimal price;

    private String currency;

    private Boolean hidden = Boolean.FALSE;

    private Licensee licensee;

    private LicenseTemplate licenseTemplate;

    private Map<String, String> licenseProperties;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(final Boolean hidden) {
        this.hidden = hidden;
    }

    public Licensee getLicensee() {
        return licensee;
    }

    public void setLicensee(final Licensee licensee) {
        licensee.getLicenses().add(this);
        this.licensee = licensee;
    }

    public LicenseTemplate getLicenseTemplate() {
        return licenseTemplate;
    }

    public void setLicenseTemplate(final LicenseTemplate licenseTemplate) {
        licenseTemplate.getLicenses().add(this);
        this.licenseTemplate = licenseTemplate;
    }

    public Map<String, String> getLicenseProperties() {
        if (licenseProperties == null) {
            licenseProperties = new HashMap<String, String>();
        }
        return licenseProperties;
    }

    public void setLicenseProperties(final Map<String, String> licenseProperties) {
        this.licenseProperties = licenseProperties;
    }

    public void addProperty(final String property, final String value) {
        getLicenseProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getLicenseProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.PRICE);
        reserved.add(Constants.CURRENCY);
        reserved.add(Constants.License.HIDDEN);
        reserved.add(Constants.Licensee.LICENSEE_NUMBER); // maps to 'licensee'
        reserved.add(Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER); // maps to 'licenseTemplate'
        reserved.add(Constants.Transaction.TRANSACTION_NUMBER); // maps to 'licenseTransactionJoins'
        reserved.add(Constants.Shop.PROP_SHOP_LICENSE_ID); // used by shop, therefore disallowed for user
        reserved.add(Constants.Shop.PROP_SHOPPING_CART); // used by shop, therefore disallowed for user
        reserved.add(Constants.Vendor.VENDOR_NUMBER); // used by shop, therefore disallowed for user
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(License.class.getSimpleName());
        builder.append(" [");
        builder.append(super.toString());
        builder.append(", ");
        builder.append(Constants.NAME);
        builder.append("=");
        builder.append(getName());
        builder.append(", ");
        builder.append(Constants.PRICE);
        builder.append("=");
        builder.append(getPrice());
        builder.append(", ");
        builder.append(Constants.CURRENCY);
        builder.append("=");
        builder.append(getCurrency());
        builder.append(", ");
        builder.append(Constants.License.HIDDEN);
        builder.append("=");
        builder.append(getHidden());
        if (licenseProperties != null) {
            for (final Map.Entry<String, String> lp : licenseProperties.entrySet()) {
                builder.append(", ");
                builder.append(lp.getKey());
                builder.append("=");
                builder.append(lp.getValue());
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
