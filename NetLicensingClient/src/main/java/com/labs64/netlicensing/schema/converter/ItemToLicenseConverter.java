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
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link License} object.
 */
public class ItemToLicenseConverter extends ItemToEntityBaseConverter<License> {

    @Override
    public License convert(final Item source) throws ConversionException {
        final License target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            target.setPrice(DatatypeConverter.parseDecimal(SchemaFunction.propertyByName(source.getProperty(),
                    Constants.PRICE).getValue()));
        }
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.CURRENCY).getValue() != null) {
            target.setCurrency(Currency.valueOf(SchemaFunction.propertyByName(source.getProperty(), Constants.CURRENCY)
                    .getValue()));
        }
        target.setHidden(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.License.HIDDEN, Boolean.FALSE.toString()).getValue()));

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!LicenseImpl.getReservedProps().contains(property.getName())) {
                target.getLicenseProperties().put(property.getName(), property.getValue());
            }
        }

        target.setLicensee(new LicenseeImpl());
        target.getLicensee()
                .setNumber(
                        SchemaFunction.propertyByName(source.getProperty(), Constants.Licensee.LICENSEE_NUMBER)
                                .getValue());

        target.setLicenseTemplate(new LicenseTemplateImpl());
        target.getLicenseTemplate()
                .setNumber(
                        SchemaFunction.propertyByName(source.getProperty(),
                                Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER)
                                .getValue());

        return target;
    }

    @Override
    public License newTarget() {
        return new LicenseImpl();
    }

}
