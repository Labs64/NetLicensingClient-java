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

import java.math.BigDecimal;
import java.util.Map;

import com.labs64.netlicensing.domain.vo.Currency;

/**
 * License entity used internally by NetLicensing.
 * <p>
 * Properties visible via NetLicensing API:
 * <p>
 * <b>number</b> - Unique number (across all products/licensees of a vendor) that identifies the license. Vendor can
 * assign this number when creating a license or let NetLicensing generate one. Read-only after corresponding creation
 * transaction status is set to closed.
 * <p>
 * <b>name</b> - Name for the licensed item. Set from license template on creation, if not specified explicitly.
 * <p>
 * <b>active</b> - If set to false, the license is disabled. License can be re-enabled, but as long as it is disabled,
 * the license is excluded from the validation process.
 * <p>
 * <b>price</b> - price for the license. If more than 0, it must always be accompanied by the currency specification. Read-only,
 * set from license template on creation.
 * <p>
 * <b>currency</b> - specifies currency for the license price. Check data types to discover which currencies are
 * supported. Read-only, set from license template on creation.
 * <p>
 * <b>hidden</b> - If set to true, this license is not shown in NetLicensing Shop as purchased license. Set from license
 * template on creation, if not specified explicitly.
 * <p>
 * Arbitrary additional user properties of string type may be associated with each license. The name of user property
 * must not be equal to any of the fixed property names listed above and must be none of <b>id, deleted, licenseeNumber,
 * licenseTemplateNumber</b>. See {@link com.labs64.netlicensing.schema.context.Property} for details.
 */
public interface License extends BaseEntity {

    // Methods for working with properties

    String getName();

    void setName(String name);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);

    Currency getCurrency();

    void setCurrency(Currency currency);

    Boolean getHidden();

    void setHidden(Boolean hidden);

    // Methods for working with custom properties

    @Deprecated
    Map<String, String> getLicenseProperties();

    // Methods for interacting with other entities

    Licensee getLicensee();

    void setLicensee(Licensee licensee);

    LicenseTemplate getLicenseTemplate();

    void setLicenseTemplate(LicenseTemplate licenseTemplate);
}
