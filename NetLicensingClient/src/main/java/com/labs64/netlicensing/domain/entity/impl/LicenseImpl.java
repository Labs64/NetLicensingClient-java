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
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.LicenseTransactionJoin;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.vo.Currency;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.License}.
 */
public class LicenseImpl extends BaseEntityImpl implements License {

    private static final long serialVersionUID = -1255007603439878867L;

    private String name;

    private BigDecimal price;

    private Currency currency;

    private Boolean hidden = Boolean.FALSE;

    private Licensee licensee;

    private LicenseTemplate licenseTemplate;

    private List<LicenseTransactionJoin> licenseTransactionJoins;

    /**
     * @see BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
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
    public Boolean getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(final Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public Licensee getLicensee() {
        return licensee;
    }

    @Override
    public void setLicensee(final Licensee licensee) {
        licensee.getLicenses().add(this);
        this.licensee = licensee;
    }

    @Override
    public LicenseTemplate getLicenseTemplate() {
        return licenseTemplate;
    }

    @Override
    public void setLicenseTemplate(final LicenseTemplate licenseTemplate) {
        licenseTemplate.getLicenses().add(this);
        this.licenseTemplate = licenseTemplate;
    }

    @Override
    public Map<String, String> getLicenseProperties() {
        return getProperties();
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.NAME, getName());
        map.add(Constants.PRICE, getPrice());
        map.add(Constants.CURRENCY, getCurrency());
        map.add(Constants.License.HIDDEN, getHidden());
        return map;
    }

    @Override
    public List<LicenseTransactionJoin> getLicenseTransactionJoins() {
        if (licenseTransactionJoins == null) {
            licenseTransactionJoins = new ArrayList<>();
        }
        return licenseTransactionJoins;
    }

    @Override
    public void setLicenseTransactionJoins(final List<LicenseTransactionJoin> licenseTransactionJoins) {
        this.licenseTransactionJoins = licenseTransactionJoins;
    }

}
