package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link LicenseTemplate} object.
 */
public class ItemToLicenseTemplateConverter extends ItemToEntityBaseConverter<LicenseTemplate> {

    @Override
    public LicenseTemplate convert(final Item source) {
        final LicenseTemplate target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setLicenseType(SchemaFunction.propertyByName(source.getProperty(),
                Constants.LicenseTemplate.LICENSE_TYPE).getValue());
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            final Money price = convertPrice(source.getProperty(), Constants.PRICE);
            target.setPrice(price.getAmount());
            target.setCurrency(price.getCurrencyCode());
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
            if (!LicenseTemplate.getReservedProps().contains(property.getName())) {
                target.getLicenseTemplateProperties().put(property.getName(), property.getValue());
            }
        }

        target.setProductModule(new ProductModule());
        target.getProductModule().setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.ProductModule.PRODUCT_MODULE_NUMBER)
                .getValue());

        return target;
    }

    @Override
    public LicenseTemplate newTarget() {
        return new LicenseTemplate();
    }

}
