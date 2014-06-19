package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.util.DateUtils;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Token} object.
 */
public class ItemToTokenConverter extends ItemToEntityBaseConverter<Token> {

    @Override
    public Token convert(final Item source) {
        final Token target = super.convert(source);

        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Token.EXPIRATION_TIME).getValue() != null) {
            target.setExpirationTime(DateUtils.parseDate(SchemaFunction.propertyByName(
                    source.getProperty(), Constants.Token.EXPIRATION_TIME).getValue()).getTime());
        }

        target.setTokenType(TokenType.parseString(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Token.TOKEN_TYPE).getValue()));

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!Token.getReservedProps().contains(property.getName())) {
                target.getTokenProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Token newTarget() {
        return new Token();
    }

}
