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
 * Product module entity used internally by NetLicensing.
 * <p/>
 * Properties visible via NetLicensing API:
 * <p/>
 * <b>number</b> - Unique number (across all products of a vendor) that identifies the product module. Vendor can assign
 * this number when creating a product module or let NetLicensing generate one. Read-only after creation of the first
 * licensee for the product.
 * <p/>
 * <b>active</b> - If set to false, the product module is disabled. Licensees can not obtain any new licenses for this
 * product module.
 * <p/>
 * <b>name</b> - Product module name that is visible to the end customers in NetLicensing Shop.
 * <p/>
 * <b>licensingModel</b> - Licensing model applied to this product module. Defines what license templates can be
 * configured for the product module and how licenses for this product module are processed during validation.
 */
public class ProductModule extends BaseEntity {

    private Product product;

    private String name;

    private String licensingModel;

    private Collection<LicenseTemplate> licenseTemplates;

    private Map<String, String> productModuleProperties;

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        product.getProductModules().add(this);
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLicensingModel() {
        return licensingModel;
    }

    public void setLicensingModel(final String licensingModel) {
        this.licensingModel = licensingModel;
    }

    public Collection<LicenseTemplate> getLicenseTemplates() {
        if (licenseTemplates == null) {
            licenseTemplates = new ArrayList<LicenseTemplate>();
        }
        return licenseTemplates;
    }

    public void setLicenseTemplates(final Collection<LicenseTemplate> licenseTemplates) {
        this.licenseTemplates = licenseTemplates;
    }

    public Map<String, String> getProductModuleProperties() {
        if (productModuleProperties == null) {
            productModuleProperties = new HashMap<String, String>();
        }
        return productModuleProperties;
    }

    public void setProductModuleProperties(final Map<String, String> productModuleProperties) {
        this.productModuleProperties = productModuleProperties;
    }

    public void addProperty(final String property, final String value) {
        getProductModuleProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getProductModuleProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.ProductModule.LICENSING_MODEL);
        reserved.add(Constants.Product.PRODUCT_NUMBER);
        reserved.add(Constants.IN_USE);
        return reserved;
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        map.put(Constants.NAME, getName());
        map.put(Constants.ProductModule.LICENSING_MODEL, getLicensingModel());
        return map;
    }

}
