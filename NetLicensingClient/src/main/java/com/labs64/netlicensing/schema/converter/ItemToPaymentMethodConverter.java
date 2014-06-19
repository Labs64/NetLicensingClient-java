package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link PaymentMethod} object.
 */
public class ItemToPaymentMethodConverter extends ItemToEntityBaseConverter<PaymentMethod> {

    @Override
    public PaymentMethod convert(final Item source) {
        final PaymentMethod target = super.convert(source);

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!PaymentMethod.getReservedProps().contains(property.getName())) {
                target.getPaymentMethodProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public PaymentMethod newTarget() {
        return new PaymentMethod();
    }

}
