/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.domain.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.ProductModule}.
 */
public class ProductModuleImpl extends BaseEntityImpl implements ProductModule {

    private static final long serialVersionUID = 6327895102315403718L;

    private Product product;

    private String name;

    private String licensingModel;

    private Collection<LicenseTemplate> licenseTemplates;

    /**
     * @see BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.ProductModule.LICENSING_MODEL);
        reserved.add(Constants.Product.PRODUCT_NUMBER);
        reserved.add(Constants.IN_USE);
        return reserved;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(final Product product) {
        product.getProductModules().add(this);
        this.product = product;
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
    public String getLicensingModel() {
        return licensingModel;
    }

    @Override
    public void setLicensingModel(final String licensingModel) {
        this.licensingModel = licensingModel;
    }

    @Override
    public Collection<LicenseTemplate> getLicenseTemplates() {
        if (licenseTemplates == null) {
            licenseTemplates = new ArrayList<LicenseTemplate>();
        }
        return licenseTemplates;
    }

    public void setLicenseTemplates(final Collection<LicenseTemplate> licenseTemplates) {
        this.licenseTemplates = licenseTemplates;
    }

    @Override
    public Map<String, String> getProductModuleProperties() {
        return getProperties();
    }

    @Override
    public Map<String, Object> asMap() {
        final Map<String, Object> map = super.asMap();
        map.put(Constants.NAME, getName());
        map.put(Constants.ProductModule.LICENSING_MODEL, getLicensingModel());
        return map;
    }

}
