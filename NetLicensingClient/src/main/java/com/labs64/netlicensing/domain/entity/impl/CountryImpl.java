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

import java.math.BigDecimal;

import javax.ws.rs.core.MultivaluedMap;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Country;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.Country}.
 */
public class CountryImpl extends BaseEntityImpl implements Country {

    private static final long serialVersionUID = -6890659657570098474L;

    private String code;

    private String name;

    private BigDecimal vat;

    private boolean isEu;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(final String code) {
        this.code = code;
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
    public void setVat(final BigDecimal vat) {
        this.vat = vat;
    }

    @Override
    public BigDecimal getVat() {
        return vat;
    }

    @Override
    public void setIsEu(final boolean isEu) {
        this.isEu = isEu;
    }

    @Override
    public boolean getIsEu() {
        return isEu;
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.Country.CODE, getCode());
        map.add(Constants.Country.NAME, getName());
        map.add(Constants.Country.VAT, getVat());
        map.add(Constants.Country.IS_EU, getIsEu());
        return map;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Country [");
        builder.append(Constants.Country.CODE);
        builder.append("=");
        builder.append(getCode());
        builder.append(", ");
        builder.append(Constants.Country.NAME);
        builder.append("=");
        builder.append(getName());
        builder.append(", ");
        builder.append(Constants.Country.VAT);
        builder.append("=");
        builder.append(getVat());
        builder.append(", ");
        builder.append(Constants.Country.IS_EU);
        builder.append("=");
        builder.append(getIsEu());
        builder.append("]");
        return builder.toString();
    }
}
