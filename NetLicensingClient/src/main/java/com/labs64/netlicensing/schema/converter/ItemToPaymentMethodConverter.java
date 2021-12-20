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

import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.impl.PaymentMethodImpl;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link PaymentMethod} object.
 */
public class ItemToPaymentMethodConverter extends ItemToEntityBaseConverter<PaymentMethod> {

    @Override
    public PaymentMethod convert(final Item source) throws ConversionException {
        final PaymentMethod target = super.convert(source);

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!PaymentMethodImpl.getReservedProps().contains(property.getName())) {
                target.getProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public PaymentMethod newTarget() {
        return new PaymentMethodImpl();
    }

}
