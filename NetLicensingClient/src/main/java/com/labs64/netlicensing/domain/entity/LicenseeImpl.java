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
 * Default implementation of {@link Licensee}.
 */
public class LicenseeImpl extends BaseEntityImpl implements Licensee {

    private Product product;

    private Collection<License> licenses;

    private Map<String, String> licenseeProperties;

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.Product.PRODUCT_NUMBER); // maps to 'product'
        reserved.add(Constants.IN_USE);
        reserved.add(Constants.Vendor.VENDOR_NUMBER); // used by shop, therefore disallowed for user
        return reserved;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(final Product product) {
        product.getLicensees().add(this);
        this.product = product;
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
    public Map<String, String> getLicenseeProperties() {
        if (licenseeProperties == null) {
            licenseeProperties = new HashMap<String, String>();
        }
        return licenseeProperties;
    }

    public void setLicenseeProperties(final Map<String, String> licenseeProperties) {
        this.licenseeProperties = licenseeProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getLicenseeProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getLicenseeProperties().remove(property);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        if (licenseeProperties != null) {
            for (Map.Entry<String, String> lp : licenseeProperties.entrySet()) {
                map.put(lp.getKey(), lp.getValue());
            }
        }
        return map;
    }

}
