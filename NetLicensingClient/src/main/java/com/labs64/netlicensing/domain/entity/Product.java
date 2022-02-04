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
package com.labs64.netlicensing.domain.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * NetLicensing Product entity.
 * <p>
 * Properties visible via NetLicensing API:
 * <p>
 * <b>number</b> - Unique number that identifies the product. Vendor can assign this number when creating a product or
 * let NetLicensing generate one. Read-only after creation of the first licensee for the product.
 * <p>
 * <b>active</b> - If set to false, the product is disabled. No new licensees can be registered for the product,
 * existing licensees can not obtain new licenses.
 * <p>
 * <b>name</b> - Product name. Together with the version identifies the product for the end customer.
 * <p>
 * <b>version</b> - Product version. Convenience parameter, additional to the product name.
 * <p>
 * <b>licenseeAutoCreate</b> - If set to 'true', non-existing licensees will be created at first validation attempt.
 * <p>
 * <b>description</b> - Product description. Optional.
 * <p>
 * <b>licensingInfo</b> - Licensing information. Optional.
 * <p>
 * Arbitrary additional user properties of string type may be associated with each product. The name of user property
 * must not be equal to any of the fixed property names listed above and must not be <b>id</b>.
 */
public interface Product extends BaseEntity {

    // Methods for working with properties

    String getName();

    void setName(String name);

    String getVersion();

    void setVersion(String version);

    Boolean getLicenseeAutoCreate();

    void setLicenseeAutoCreate(Boolean licenseeAutoCreate);

    String getDescription();

    void setDescription(String description);

    String getLicensingInfo();

    void setLicensingInfo(String licensingInfo);

    // Methods for working with custom properties

    @Deprecated
    Map<String, String> getProductProperties();

    // Methods for interacting with other entities

    Collection<ProductModule> getProductModules();

    Collection<Licensee> getLicensees();

    List<ProductDiscount> getProductDiscounts();

    void addDiscount(ProductDiscount discount);
}
