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
package com.labs64.netlicensing.schema.converter;

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Country;
import com.labs64.netlicensing.domain.entity.impl.CountryImpl;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;

/**
 * Convert {@link Item} entity into {@link Country} object.
 */
public class ItemToCountryConverter extends ItemToEntityBaseConverter<Country> {

    @Override
    public Country convert(final Item source) throws ConversionException {
        final Country target = super.convert(source);

        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Country.CODE).getValue() != null) {
            target.setCode(SchemaFunction.propertyByName(source.getProperty(), Constants.Country.CODE).getValue());
        }
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Country.NAME).getValue() != null) {
            target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.Country.NAME).getValue());
        }
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Country.VAT_PERCENT).getValue() != null) {
            target.setVatPercent(DatatypeConverter.parseDecimal(
                    SchemaFunction.propertyByName(source.getProperty(), Constants.Country.VAT_PERCENT).getValue()));
        }
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Country.IS_EU).getValue() != null) {
            target.setIsEu(Boolean
                    .valueOf(SchemaFunction.propertyByName(source.getProperty(), Constants.Country.IS_EU).getValue()));
        }
        return target;
    }

    @Override
    public Country newTarget() {
        return new CountryImpl();
    }

}
