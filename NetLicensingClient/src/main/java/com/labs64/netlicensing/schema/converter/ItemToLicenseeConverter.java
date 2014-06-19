package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Licensee} object.
 */
public class ItemToLicenseeConverter extends ItemToEntityBaseConverter<Licensee> {

    @Override
    public Licensee convert(final Item source) {
        final Licensee target = super.convert(source);

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!Licensee.getReservedProps().contains(property.getName())) {
                target.getLicenseeProperties().put(property.getName(), property.getValue());
            }
        }

        target.setProduct(new Product());
        target.getProduct().setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.Product.PRODUCT_NUMBER).getValue());

        return target;
    }

    @Override
    public Licensee newTarget() {
        return new Licensee();
    }

}
