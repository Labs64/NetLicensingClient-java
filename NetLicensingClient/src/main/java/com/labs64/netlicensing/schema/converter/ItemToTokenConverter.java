/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.impl.TokenImpl;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Convert {@link Item} entity into {@link Token} object.
 */
public class ItemToTokenConverter extends ItemToEntityBaseConverter<Token> {

    @Override
    public Token convert(final Item source) throws ConversionException {
        final Token target = super.convert(source);

        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Token.EXPIRATION_TIME).getValue() != null) {
            target.setExpirationTime(DateUtils.parseDate(SchemaFunction.propertyByName(
                    source.getProperty(), Constants.Token.EXPIRATION_TIME).getValue()).getTime());
        }

        target.setTokenType(TokenType.parseString(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Token.TOKEN_TYPE).getValue()));
        target.setVendorNumber(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Token.TOKEN_PROP_VENDORNUMBER).getValue());

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!TokenImpl.getReservedProps().contains(property.getName())) {
                target.getProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Token newTarget() {
        return new TokenImpl();
    }

}
