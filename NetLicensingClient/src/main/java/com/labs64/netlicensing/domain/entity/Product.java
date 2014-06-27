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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;

/**
 * Product entity used internally by NetLicensing.
 * <p/>
 * Properties visible via NetLicensing API:
 * <p/>
 * <b>number</b> - Unique number that identifies the product. Vendor can assign this number when creating a product or
 * let NetLicensing generate one. Read-only after creation of the first licensee for the product.
 * <p/>
 * <b>active</b> - If set to false, the product is disabled. No new licensees can be registered for the product,
 * existing licensees can not obtain new licenses.
 * <p/>
 * <b>name</b> - Product name. Together with the version identifies the product for the end customer.
 * <p/>
 * <b>version</b> - Product version. Convenience parameter, additional to the product name.
 * <p/>
 * <b>licenseeAutoCreate</b> - If set to 'true', non-existing licensees will be created at first validation attempt.
 * <p/>
 * <b>description</b> - Product description. Optional.
 * <p/>
 * <b>licensingInfo</b> - Licensing information. Optional.
 * <p/>
 * Arbitrary additional user properties of string type may be associated with each product. The name of user property
 * must not be equal to any of the fixed property names listed above and must be none of <b>id, deleted</b>.
 */
public class Product extends BaseEntity {

    private String name;

    private String version;

    private Boolean licenseeAutoCreate = Boolean.FALSE;

    private String description;

    private String licensingInfo;

    private Collection<ProductModule> productModules;

    private Collection<Licensee> licensees;

    private List<ProductDiscount> productDiscounts;

    private Map<String, String> productProperties;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public Boolean getLicenseeAutoCreate() {
        return licenseeAutoCreate;
    }

    public void setLicenseeAutoCreate(final Boolean licenseeAutoCreate) {
        this.licenseeAutoCreate = licenseeAutoCreate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLicensingInfo() {
        return licensingInfo;
    }

    public void setLicensingInfo(final String licensingInfo) {
        this.licensingInfo = licensingInfo;
    }

    public Collection<ProductModule> getProductModules() {
        if (productModules == null) {
            productModules = new ArrayList<ProductModule>();
        }
        return productModules;
    }

    public void setProductModules(final Collection<ProductModule> productModules) {
        this.productModules = productModules;
    }

    public Collection<Licensee> getLicensees() {
        if (licensees == null) {
            licensees = new ArrayList<Licensee>();
        }
        return licensees;
    }

    public void setLicensees(final Collection<Licensee> licensees) {
        this.licensees = licensees;
    }

    public List<ProductDiscount> getProductDiscounts() {
        if (productDiscounts == null) {
            productDiscounts = new ArrayList<ProductDiscount>();
        }
        return productDiscounts;
    }

    public void setProductDiscounts(final List<ProductDiscount> productDiscounts) {
        this.productDiscounts = productDiscounts;
        for (final ProductDiscount productDiscount : this.productDiscounts) {
            productDiscount.setProduct(this);
        }
    }

    public void addDiscount(final ProductDiscount discount) {
        discount.setProduct(this);
        getProductDiscounts().add(discount);
    }

    public Map<String, String> getProductProperties() {
        if (productProperties == null) {
            productProperties = new HashMap<String, String>();
        }
        return productProperties;
    }

    public void addProperty(final String property, final String value) {
        getProductProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getProductProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.VERSION);
        reserved.add(Constants.Product.LICENSEE_AUTO_CREATE);
        reserved.add(Constants.Product.DESCRIPTION);
        reserved.add(Constants.Product.LICENSING_INFO);
        reserved.add(Constants.DISCOUNT);
        reserved.add(Constants.IN_USE);
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Product [");
        builder.append(super.toString());
        builder.append(", ");
        builder.append(Constants.NAME);
        builder.append("=");
        builder.append(getName());
        builder.append(", ");
        builder.append(Constants.VERSION);
        builder.append("=");
        builder.append(getVersion());
        builder.append(", ");
        builder.append(Constants.Product.LICENSEE_AUTO_CREATE);
        builder.append("=");
        builder.append(String.valueOf(getLicenseeAutoCreate()));
        if (getDescription() != null) {
            builder.append(", ");
            builder.append(Constants.Product.DESCRIPTION);
            builder.append("=");
            builder.append((getDescription().length() > 50) ? getDescription().substring(0, 50) : getDescription());
        }
        if (getLicensingInfo() != null) {
            builder.append(", ");
            builder.append(Constants.Product.LICENSING_INFO);
            builder.append("=");
            builder.append((getLicensingInfo().length() > 50) ? getLicensingInfo().substring(0, 50)
                    : getLicensingInfo());
        }
        for (final ProductDiscount discount : getProductDiscounts()) {
            builder.append(", ");
            builder.append(discount.toString());
        }
        if (productProperties != null) {
            for (final Map.Entry<String, String> property : productProperties.entrySet()) {
                builder.append(", ");
                builder.append(property.getKey());
                builder.append("=");
                builder.append(property.getValue());
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
