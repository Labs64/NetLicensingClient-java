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
 * Default implementation of {@link Product}.
 */
public class ProductImpl extends BaseEntityImpl implements Product {

    private String name;

    private String version;

    private Boolean licenseeAutoCreate;

    private String description;

    private String licensingInfo;

    private Collection<ProductModule> productModules;

    private Collection<Licensee> licensees;

    private List<ProductDiscount> productDiscounts;

    private Map<String, String> productProperties;

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public Boolean getLicenseeAutoCreate() {
        return licenseeAutoCreate;
    }

    @Override
    public void setLicenseeAutoCreate(final Boolean licenseeAutoCreate) {
        this.licenseeAutoCreate = licenseeAutoCreate;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getLicensingInfo() {
        return licensingInfo;
    }

    @Override
    public void setLicensingInfo(final String licensingInfo) {
        this.licensingInfo = licensingInfo;
    }

    @Override
    public Collection<ProductModule> getProductModules() {
        if (productModules == null) {
            productModules = new ArrayList<ProductModule>();
        }
        return productModules;
    }

    public void setProductModules(final Collection<ProductModule> productModules) {
        this.productModules = productModules;
    }

    @Override
    public Collection<Licensee> getLicensees() {
        if (licensees == null) {
            licensees = new ArrayList<Licensee>();
        }
        return licensees;
    }

    public void setLicensees(final Collection<Licensee> licensees) {
        this.licensees = licensees;
    }

    @Override
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

    @Override
    public void addDiscount(final ProductDiscount discount) {
        discount.setProduct(this);
        getProductDiscounts().add(discount);
    }

    @Override
    public Map<String, String> getProductProperties() {
        if (productProperties == null) {
            productProperties = new HashMap<String, String>();
        }
        return productProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getProductProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getProductProperties().remove(property);
    }

    @Override
    public String toString() {
        final Map<String, Object> propMap = asPropertiesMap();
        propMap.put(Constants.Product.DISCOUNTS, getProductDiscounts());
        return toString(propMap);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        map.put(Constants.NAME, getName());
        map.put(Constants.VERSION, getVersion());
        map.put(Constants.Product.LICENSEE_AUTO_CREATE, getLicenseeAutoCreate());
        map.put(Constants.Product.DESCRIPTION, getDescription());
        map.put(Constants.Product.LICENSING_INFO, getLicensingInfo());
        if (productProperties != null) {
            for (final Map.Entry<String, String> property : productProperties.entrySet()) {
                map.put(property.getKey(), property.getValue());
            }
        }
        return map;
    }

}
