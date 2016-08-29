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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;

/**
 * License template entity used internally by NetLicensing.
 * <p/>
 * Properties visible via NetLicensing API:
 * <p/>
 * <b>number</b> - Unique number (across all products of a vendor) that identifies the license template. Vendor can
 * assign this number when creating a license template or let NetLicensing generate one. Read-only after creation of the
 * first license from this license template.
 * <p/>
 * <b>active</b> - If set to false, the license template is disabled. Licensee can not obtain any new licenses off this
 * license template.
 * <p/>
 * <b>name</b> - Name for the licensed item.
 * <p/>
 * <b>licenseType</b> - type of licenses created from this license template. Supported types: "FEATURE", "TIMEVOLUME".
 * <p/>
 * <b>price</b> - price for the license. If >0, it must always be accompanied by the currency specification.
 * <p/>
 * <b>currency</b> - specifies currency for the license price. Check data types to discover which currencies are
 * supported.
 * <p/>
 * <b>automatic</b> - If set to true, every new licensee automatically gets one license out of this license template on
 * creation. Automatic licenses must have their price set to 0.
 * <p/>
 * <b>hidden</b> - If set to true, this license template is not shown in NetLicensing Shop as offered for purchase.
 * <p/>
 * <b>hideLicenses</b> - If set to true, licenses from this license template are not visible to the end customer, but
 * participate in validation.
 * <p/>
 */
public interface LicenseTemplate extends BaseEntity {

    // Methods for working with properties

    String getName();

    void setName(String name);

    LicenseType getLicenseType();

    void setLicenseType(LicenseType licenseType);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);

    Currency getCurrency();

    void setCurrency(Currency currency);

    Boolean getAutomatic();

    void setAutomatic(Boolean automatic);

    Boolean getHidden();

    void setHidden(Boolean hidden);

    Boolean getHideLicenses();

    void setHideLicenses(Boolean hideLicenses);

    // Methods for working with custom properties

    @Deprecated
    Map<String, String> getLicenseTemplateProperties();

    // Methods for interacting with other entities

    ProductModule getProductModule();

    void setProductModule(ProductModule productModule);

    Collection<License> getLicenses();

}
