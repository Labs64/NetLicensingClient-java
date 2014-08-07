package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.LicensingModelProperties;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;

/**
 * Converts {@link Item} entity into an implementation of {@link LicensingModelProperties} interface.
 */
public class ItemToLicensingModelPropertiesConverter implements Converter<Item, LicensingModelProperties> {

    @Override
    public LicensingModelProperties convert(final Item source) throws ConversionException {
        if (!Constants.Utility.LICENSING_MODEL_PROPERTIES.equals(source.getType())) {
            final String sourceType = (source.getType() != null) ? source.getType() : "<null>";
            throw new ConversionException(String.format("Wrong item type '%s', expected '%s'", sourceType,
                    Constants.Utility.LICENSING_MODEL_PROPERTIES));
        }

        final String name = SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue();
        return new LicensingModelPropertiesImpl(name);
    }

    private static class LicensingModelPropertiesImpl implements LicensingModelProperties {

        private String name;

        public LicensingModelPropertiesImpl(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

    }
}
