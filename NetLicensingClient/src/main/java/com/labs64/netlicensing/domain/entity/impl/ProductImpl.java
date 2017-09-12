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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductDiscount;
import com.labs64.netlicensing.domain.entity.ProductModule;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.Product}.
 */
public class ProductImpl extends BaseEntityImpl implements Product {

    private static final long serialVersionUID = 7030811820855516068L;

    private String name;

    private String version;

    private Boolean licenseeAutoCreate;

    private String description;

    private String licensingInfo;

    private Collection<ProductModule> productModules;

    private Collection<Licensee> licensees;

    private List<ProductDiscount> productDiscounts;

    private Boolean productDiscountsTouched = false;

    /**
     * @see BaseEntityImpl#getReservedProps()
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
            productModules = new ArrayList<>();
        }
        return productModules;
    }

    public void setProductModules(final Collection<ProductModule> productModules) {
        this.productModules = productModules;
    }

    @Override
    public Collection<Licensee> getLicensees() {
        if (licensees == null) {
            licensees = new ArrayList<>();
        }
        return licensees;
    }

    public void setLicensees(final Collection<Licensee> licensees) {
        this.licensees = licensees;
    }

    @Override
    public List<ProductDiscount> getProductDiscounts() {
        if (productDiscounts == null) {
            productDiscounts = new ArrayList<>();
        }
        return productDiscounts;
    }

    public void setProductDiscounts(final List<ProductDiscount> productDiscounts) {
        this.productDiscounts = productDiscounts;
        for (final ProductDiscount productDiscount : this.productDiscounts) {
            productDiscount.setProduct(this);
        }
        productDiscountsTouched = true;
    }

    public void setProductDiscounts() {
        this.productDiscounts = new ArrayList<>();
        productDiscountsTouched = true;
    }

    @Override
    public void addDiscount(final ProductDiscount discount) {
        discount.setProduct(this);
        getProductDiscounts().add(discount);
        productDiscountsTouched = true;
    }

    @Override
    public Map<String, String> getProductProperties() {
        return getProperties();
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.NAME, getName());
        map.add(Constants.VERSION, getVersion());
        map.add(Constants.Product.LICENSEE_AUTO_CREATE, getLicenseeAutoCreate());
        map.add(Constants.Product.DESCRIPTION, getDescription());
        map.add(Constants.Product.LICENSING_INFO, getLicensingInfo());
        if (productDiscounts != null) {
            for (final ProductDiscount productDiscount : productDiscounts) {
                map.add(Constants.DISCOUNT, productDiscount.toString());
            }
        }

        if (map.get(Constants.DISCOUNT) == null && productDiscountsTouched) {
            map.add(Constants.DISCOUNT, "");
        }

        return map;
    }

}
