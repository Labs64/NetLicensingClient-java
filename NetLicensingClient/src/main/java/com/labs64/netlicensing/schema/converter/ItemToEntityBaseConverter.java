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

import java.util.List;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.BaseEntity;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link BaseEntity} object.
 */
abstract class ItemToEntityBaseConverter<T extends BaseEntity> implements Converter<Item, T> {

    protected abstract T newTarget();

    @Override
    public T convert(final Item source) throws ConversionException {
        final T target = newTarget();

        final String entityClass = target.getClass().getInterfaces()[0].getSimpleName();
        if (!entityClass.equals(source.getType())) {
            final String sourceType = (source.getType() != null) ? source.getType() : "<null>";
            throw new ConversionException(String.format("Wrong item type '%s', expected '%s'", sourceType, entityClass));
        }

        target.setActive(Boolean.parseBoolean(
                SchemaFunction.propertyByName(source.getProperty(), Constants.ACTIVE, Boolean.FALSE.toString())
                        .getValue()));
        target.setNumber(SchemaFunction.propertyByName(source.getProperty(), Constants.NUMBER).getValue());

        return target;
    }

    /**
     * Converts price with currency from NetLicensing XML representation to the internal Money value.
     * 
     * @param source
     *            - collection of properties from NetLicensing XML
     * @param priceProperty
     *            - the property name holding price value, currency always assumed in Constants.CURRENCY
     * @return converted money object
     */
    static Money convertPrice(final List<Property> source, final String priceProperty) {
        final String rawPrice = SchemaFunction.propertyByName(source, priceProperty).getValue();
        final String rawCurrency = SchemaFunction.propertyByName(source, Constants.CURRENCY).getValue();
        return Money.convertPrice(rawPrice, rawCurrency);
    }

}
