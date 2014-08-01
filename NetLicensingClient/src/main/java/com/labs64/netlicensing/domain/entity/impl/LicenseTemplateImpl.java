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
package com.labs64.netlicensing.domain.entity.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.LicenseTemplate}.
 */
public class LicenseTemplateImpl extends BaseEntityImpl implements LicenseTemplate {

    private ProductModule productModule;

    private String name;

    private LicenseType licenseType;

    private BigDecimal price;

    private Currency currency;

    private Boolean automatic = Boolean.FALSE;

    private Boolean hidden = Boolean.FALSE;

    private Boolean hideLicenses = Boolean.FALSE;

    private Collection<License> licenses;

    private Map<String, String> licenseTemplateProperties;

    /**
     * @see BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
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
    public ProductModule getProductModule() {
        return productModule;
    }

    @Override
    public void setProductModule(final ProductModule productModule) {
        productModule.getLicenseTemplates().add(this);
        this.productModule = productModule;
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
    public LicenseType getLicenseType() {
        return licenseType;
    }

    @Override
    public void setLicenseType(final LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    @Override
    public Boolean getAutomatic() {
        return automatic;
    }

    @Override
    public void setAutomatic(final Boolean automatic) {
        this.automatic = automatic;
    }

    @Override
    public Boolean getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(final Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public Boolean getHideLicenses() {
        return hideLicenses;
    }

    @Override
    public void setHideLicenses(final Boolean hideLicenses) {
        this.hideLicenses = hideLicenses;
    }

    @Override
    public Collection<License> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<License>();
        }
        return licenses;
    }

    public void setLicenses(final Collection<License> licenses) {
        this.licenses = licenses;
    }

    @Override
    public Map<String, String> getLicenseTemplateProperties() {
        if (licenseTemplateProperties == null) {
            licenseTemplateProperties = new HashMap<String, String>();
        }
        return licenseTemplateProperties;
    }

    public void setLicenseTemplateProperties(final Map<String, String> licenseTemplateProperties) {
        this.licenseTemplateProperties = licenseTemplateProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getLicenseTemplateProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getLicenseTemplateProperties().remove(property);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        map.put(Constants.NAME, getName());
        map.put(Constants.LicenseTemplate.LICENSE_TYPE, getLicenseType());
        map.put(Constants.PRICE, getPrice());
        map.put(Constants.CURRENCY, getCurrency());
        map.put(Constants.LicenseTemplate.AUTOMATIC, getAutomatic());
        map.put(Constants.LicenseTemplate.HIDDEN, getHidden());
        map.put(Constants.LicenseTemplate.HIDE_LICENSES, getHideLicenses());
        if (licenseTemplateProperties != null) {
            for (final Map.Entry<String, String> ltp : licenseTemplateProperties.entrySet()) {
                map.put(ltp.getKey(), ltp.getValue());
            }
        }
        return map;
    }

}
