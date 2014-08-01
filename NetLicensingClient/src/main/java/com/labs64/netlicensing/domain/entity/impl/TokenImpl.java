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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.vo.TokenType;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.Token}.
 */
public class TokenImpl extends BaseEntityImpl implements Token {

    private String vendorNumber;

    private Date expirationTime;

    private TokenType tokenType;

    private Map<String, String> tokenProperties;

    /**
     * @see BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.Token.EXPIRATION_TIME);
        reserved.add(Constants.Token.TOKEN_TYPE);
        return reserved;
    }

    @Override
    public String getVendorNumber() {
        return vendorNumber;
    }

    @Override
    public void setVendorNumber(final String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    @Override
    public Date getExpirationTime() {
        if (expirationTime == null) {
            return null;
        } else {
            return new Date(expirationTime.getTime());
        }
    }

    @Override
    public void setExpirationTime(final Date expirationTime) {
        if (expirationTime == null) {
            this.expirationTime = null;
        } else {
            this.expirationTime = new Date(expirationTime.getTime());
        }
    }

    @Override
    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public void setTokenType(final TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public Map<String, String> getTokenProperties() {
        if (tokenProperties == null) {
            tokenProperties = new HashMap<String, String>();
        }
        return tokenProperties;
    }

    public void setTokenProperties(final Map<String, String> tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getTokenProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getTokenProperties().remove(property);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        map.put(Constants.Token.EXPIRATION_TIME, getExpirationTime());
        map.put(Constants.Token.TOKEN_TYPE, getTokenType());
        map.put(Constants.Token.TOKEN_PROP_VENDORNUMBER, getVendorNumber());
        if (tokenProperties != null) {
            for (final Map.Entry<String, String> property : tokenProperties.entrySet()) {
                map.put(property.getKey(), property.getValue());
            }
        }
        return map;
    }

}
