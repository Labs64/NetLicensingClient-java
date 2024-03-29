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
package com.labs64.netlicensing.schema.converter;

import java.util.Arrays;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Bundle;
import com.labs64.netlicensing.domain.entity.impl.BundleImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Bundle} object.
 */
public class ItemToBundleConverter extends ItemToEntityBaseConverter<Bundle> {

    @Override
    public Bundle convert(final Item source) throws ConversionException {
        final Bundle target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setDescription(SchemaFunction.propertyByName(source.getProperty(), Constants.Bundle.DESCRIPTION).getValue());
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            final Money price = convertPrice(source.getProperty(), Constants.PRICE);
            target.setPrice(price.getAmount());
            target.setCurrency(Currency.valueOf(price.getCurrencyCode()));
        }

        final String licenseTemplateNumbers = SchemaFunction.propertyByName(source.getProperty(), Constants.Bundle.LICENSE_TEMPLATE_NUMBERS).getValue();

        if (licenseTemplateNumbers != null) {
            target.setLicenseTemplateNumbers(Arrays.asList(licenseTemplateNumbers.split(",")));
        }
        
        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!LicenseTemplateImpl.getReservedProps().contains(property.getName())) {
                target.getProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Bundle newTarget() {
        return new BundleImpl();
    }

}
