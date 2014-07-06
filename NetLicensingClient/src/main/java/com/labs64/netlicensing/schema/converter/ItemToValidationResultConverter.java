package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.ValidationResult;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link ValidationResult} object.
 */
public class ItemToValidationResultConverter implements Converter<Item, ValidationResult> {

    @Override
    public ValidationResult convert(Item source) {
        final Composition composition = new Composition();
        String productModuleNumber = null;
        for (Property property : source.getProperty()) {
            if (Constants.ProductModule.PRODUCT_MODULE_NUMBER.equals(property.getName())) {
                productModuleNumber = property.getValue();
            } else {
                composition.put(property.getName(), property.getValue());
            }
        }

        final ValidationResult target = new ValidationResult();
        target.setProductModuleValidation(productModuleNumber, composition);
        return target;
    }

}
