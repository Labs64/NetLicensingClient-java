package com.labs64.netlicensing.schema.converter;

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link License} object.
 */
public class ItemToLicenseConverter extends ItemToEntityBaseConverter<License> {

    @Override
    public License convert(final Item source) {
        final License target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            target.setPrice(DatatypeConverter.parseDecimal(SchemaFunction.propertyByName(source.getProperty(),
                    Constants.PRICE).getValue()));
        }
        target.setCurrency(SchemaFunction.propertyByName(source.getProperty(), Constants.CURRENCY).getValue());
        target.setHidden(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.License.HIDDEN, Boolean.FALSE.toString()).getValue()));

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!License.getReservedProps().contains(property.getName())) {
                target.getLicenseProperties().put(property.getName(), property.getValue());
            }
        }

        target.setLicensee(new Licensee());
        target.getLicensee()
        .setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.Licensee.LICENSEE_NUMBER)
                .getValue());

        target.setLicenseTemplate(new LicenseTemplate());
        target.getLicenseTemplate()
        .setNumber(
                SchemaFunction.propertyByName(source.getProperty(),
                        Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER)
                        .getValue());

        return target;
    }

    @Override
    public License newTarget() {
        return new License();
    }

}
