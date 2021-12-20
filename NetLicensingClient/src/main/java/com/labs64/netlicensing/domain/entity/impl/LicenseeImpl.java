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
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.Product;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.Licensee}.
 */
public class LicenseeImpl extends BaseEntityImpl implements Licensee {

    private static final long serialVersionUID = 2704374141788131247L;

    private Product product;

    private Collection<License> licenses;

    /**
     * @see BaseEntityImpl#getReservedProps()
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
        return getProperties();
    }

}
