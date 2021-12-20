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

import java.util.Date;
import java.util.Map;

import com.labs64.netlicensing.domain.vo.ITokenType;

/**
 * Token entity used internally by NetLicensing.
 */
public interface Token extends BaseEntity {

    // Methods for working with properties

    String getVendorNumber();

    void setVendorNumber(String vendorNumber);

    Date getExpirationTime();

    void setExpirationTime(Date expirationTime);

    ITokenType getTokenType();

    void setTokenType(ITokenType tokenType);

    // Methods for working with custom properties

    @Deprecated
    Map<String, String> getTokenProperties();

}
