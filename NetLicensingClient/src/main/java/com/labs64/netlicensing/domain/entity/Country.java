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

/**
 * Country entity used internally by NetLicensing.
 * <p>
 * Properties visible via NetLicensing API:
 * <p>
 * <b>code</b> - Unique code of country.
 * <p>
 * <b>name</b> - Unique name of country
 * <p>
 * <b>vat</b> - Country vat.
 * <p>
 * <b>isEu</b> - is country in EU.
 * <p>
 */
public interface Country extends BaseEntity {

    void setCode(final String code);

    String getCode();

    void setName(final String name);

    String getName();

    void setVatPercent(final BigDecimal vat);

    BigDecimal getVatPercent();

    void setIsEu(final boolean isEu);

    boolean getIsEu();
}
