package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.Constants.Licensee;
import com.labs64.netlicensing.domain.vo.Composition;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.List;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.Netlicensing.Items;
import com.labs64.netlicensing.schema.context.Property;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Convert {@link Items} object into {@link ValidationResult} entity.
 */
public class ItemsToValidationResultConverter implements Converter<Netlicensing, ValidationResult> {

    @Override
    public ValidationResult convert(final Netlicensing source) throws ConversionException {
        final ValidationResult target = new ValidationResult();
        if (source == null) {
            return target;
        }

        if (source.getTtl() != null) {
            target.setTtl(DateUtils.parseDate(source.getTtl().toXMLFormat()));
        }

        if (source.getItems() == null) {
            return target;
        }

        String licenseeNumber = null;
        for (final Item item : source.getItems().getItem()) {
            if (Licensee.class.getSimpleName().equals(item.getType())) {
                for (final Property property : item.getProperty()) {
                    if (Constants.Licensee.LICENSEE_NUMBER.equals(property.getName())) {
                        licenseeNumber = property.getValue();
                        break;
                    }
                }
                continue;
            }

            if (!Constants.ValidationResult.VALIDATION_RESULT_TYPE.equals(item.getType())) {
                continue;
            }

            final Composition composition = new Composition();

            // convert properties
            String productModuleNumber = null;
            for (final Property property : item.getProperty()) {
                if (Constants.ProductModule.PRODUCT_MODULE_NUMBER.equals(property.getName())) {
                    productModuleNumber = property.getValue();
                } else {
                    composition.put(property.getName(), property.getValue());
                }
            }

            // convert lists
            if (item.getList() != null) {
                for (final List list : item.getList()) {
                    composition.put(list.getName(), convertFromList(list));
                }
            }

            if (productModuleNumber == null) {
                throw new ConversionException(String.format("Validation item does not contain property '%s'.",
                        Constants.ProductModule.PRODUCT_MODULE_NUMBER));
            }

            target.setProductModuleValidation(productModuleNumber, composition);
        }
        target.setLicenseeNumber(licenseeNumber);
        return target;
    }

    /**
     * @param list
     *            list to be converted
     * @return converted {@link Composition} object
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
