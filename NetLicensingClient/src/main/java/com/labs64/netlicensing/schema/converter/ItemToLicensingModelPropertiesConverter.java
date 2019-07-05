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
            throw new ConversionException(String.format("Wrong item type '%s', expected '%s'.", sourceType,
                    Constants.Utility.LICENSING_MODEL_PROPERTIES));
        }

        final String name = SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue();
        return new LicensingModelPropertiesImpl(name);
    }

    private static class LicensingModelPropertiesImpl implements LicensingModelProperties {

        private final String name;

        public LicensingModelPropertiesImpl(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

    }

}
