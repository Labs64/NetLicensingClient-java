package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link ProductModule} object.
 */
public class ItemToProductModuleConverter extends ItemToEntityBaseConverter<ProductModule> {

    @Override
    public ProductModule convert(final Item source) {
        final ProductModule target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setLicensingModel(SchemaFunction.propertyByName(source.getProperty(),
                Constants.ProductModule.LICENSING_MODEL).getValue());

        target.setProduct(new Product());
        target.getProduct().setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.Product.PRODUCT_NUMBER).getValue());

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!ProductModule.getReservedProps().contains(property.getName())) {
                target.getProductModuleProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public ProductModule newTarget() {
        return new ProductModule();
    }

}
