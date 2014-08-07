package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;

/**
 * Converts {@link Item} entity into an implementation of
 * {@link LicenseTypeProperties} interface.
 */
public class ItemToLicenseTypePropertiesConverter implements Converter<Item, LicenseTypeProperties> {

    @Override
    public LicenseTypeProperties convert(final Item source) throws ConversionException {
        if (!Constants.Utility.LICENSE_TYPE.equals(source.getType())) {
            final String sourceType = (source.getType() != null) ? source.getType() : "<null>";
            throw new ConversionException(String.format("Wrong item type '%s', expected '%s'", sourceType,
                    Constants.Utility.LICENSE_TYPE));
        }

        final String name = SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue();
        return new LicenseTypePropertiesImpl(name);
    }

    private static class LicenseTypePropertiesImpl implements LicenseTypeProperties {

        private String name;

        public LicenseTypePropertiesImpl(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

    }
}
