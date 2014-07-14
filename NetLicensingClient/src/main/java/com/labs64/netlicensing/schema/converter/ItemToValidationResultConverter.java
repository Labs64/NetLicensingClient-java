package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.ValidationResult;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.List;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link ValidationResult} object.
 */
public class ItemToValidationResultConverter implements Converter<Item, ValidationResult> {

    @Override
    public ValidationResult convert(final Item source) throws ConversionException {
        if (!Constants.ValidationResult.VALIDATION_RESULT_TYPE.equals(source.getType())) {
            final String sourceType = (source.getType() != null) ? source.getType() : "<null>";
            throw new ConversionException(String.format("Wrong item type '%s', expected '%s'", sourceType, Constants.ValidationResult.VALIDATION_RESULT_TYPE));
        }

        final Composition composition = new Composition();

        // convert properties
        String productModuleNumber = null;
        for (final Property property : source.getProperty()) {
            if (Constants.ProductModule.PRODUCT_MODULE_NUMBER.equals(property.getName())) {
                productModuleNumber = property.getValue();
            } else {
                composition.put(property.getName(), property.getValue());
            }
        }

        // convert lists
        if (source.getList() != null) {
            for (final List list : source.getList()) {
                composition.put(list.getName(), convertFromList(list));
            }
        }

        if (productModuleNumber == null) {
            throw new ConversionException(String.format("Validation item does not contain property '%s'", Constants.ProductModule.PRODUCT_MODULE_NUMBER));
        }

        final ValidationResult target = new ValidationResult();
        target.setProductModuleValidation(productModuleNumber, composition);
        return target;
    }

    /**
     * @param list
     * @return
     */
    private Composition convertFromList(final List list) {
        final Composition composition = new Composition();
        // convert properties
        if (list.getProperty() != null) {
            for (final Property property : list.getProperty()) {
                composition.put(property.getName(), property.getValue());
            }
        }
        // convert lists
        if (list.getList() != null) {
            for (final List sublist : list.getList()) {
                composition.put(list.getName(), convertFromList(sublist));
            }
        }
        return composition;
    }

}
