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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.TokenType;

/**
 * Token entity used internally by NetLicensing.
 */
public class Token extends BaseEntity {

    private String vendorNumber;

    private Date expirationTime;

    private TokenType tokenType;

    private Map<String, String> tokenProperties;

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNUmber(final String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public Date getExpirationTime() {
        if (expirationTime == null) {
            return null;
        } else {
            return new Date(expirationTime.getTime());
        }
    }

    public void setExpirationTime(final Date expirationTime) {
        if (expirationTime == null) {
            this.expirationTime = null;
        } else {
            this.expirationTime = new Date(expirationTime.getTime());
        }
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(final TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Map<String, String> getTokenProperties() {
        if (tokenProperties == null) {
            tokenProperties = new HashMap<String, String>();
        }
        return tokenProperties;
    }

    public void setTokenProperties(final Map<String, String> tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    public void addProperty(final String property, final String value) {
        getTokenProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getTokenProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntity.getReservedProps();
        reserved.add(Constants.Token.EXPIRATION_TIME);
        reserved.add(Constants.Token.TOKEN_TYPE);
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Token [");
        builder.append(super.toString());
        builder.append(", ");
        builder.append("expirationTime");
        builder.append("=");
        builder.append(getExpirationTime());
        builder.append(", ");
        builder.append("tokenType");
        builder.append("=");
        builder.append(getTokenType());
        builder.append(", ");
        if (tokenProperties != null) {
            for (final Map.Entry<String, String> property : tokenProperties.entrySet()) {
                builder.append(", ");
                builder.append(property.getKey());
                builder.append("=");
                builder.append(property.getValue());
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
