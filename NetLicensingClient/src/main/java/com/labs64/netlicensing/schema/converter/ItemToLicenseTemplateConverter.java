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

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.entity.ProductModuleImpl;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link LicenseTemplate} object.
 */
public class ItemToLicenseTemplateConverter extends ItemToEntityBaseConverter<LicenseTemplate> {

    @Override
    public LicenseTemplate convert(final Item source) throws ConversionException {
        final LicenseTemplate target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setLicenseType(LicenseType.valueOf(SchemaFunction.propertyByName(source.getProperty(),
                Constants.LicenseTemplate.LICENSE_TYPE).getValue()));
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            final Money price = convertPrice(source.getProperty(), Constants.PRICE);
            target.setPrice(price.getAmount());
            target.setCurrency(Currency.valueOf(price.getCurrencyCode()));
        }
        target.setAutomatic(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.LicenseTemplate.AUTOMATIC,
                Boolean.FALSE.toString()).getValue()));
        target.setHidden(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.LicenseTemplate.HIDDEN,
                Boolean.FALSE.toString()).getValue()));
        target.setHideLicenses(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.LicenseTemplate.HIDE_LICENSES,
                Boolean.FALSE.toString()).getValue()));

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!LicenseTemplateImpl.getReservedProps().contains(property.getName())) {
                target.getLicenseTemplateProperties().put(property.getName(), property.getValue());
            }
        }

        target.setProductModule(new ProductModuleImpl());
        target.getProductModule().setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.ProductModule.PRODUCT_MODULE_NUMBER)
                        .getValue());

        return target;
    }

    @Override
    public LicenseTemplate newTarget() {
        return new LicenseTemplateImpl();
    }

}
