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

import java.util.Collections;

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.impl.ProductDiscount;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Product} object.
 */
public class ItemToProductConverter extends ItemToEntityBaseConverter<Product> {

    @Override
    public Product convert(final Item source) throws ConversionException {
        final Product target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setVersion(SchemaFunction.propertyByName(source.getProperty(), Constants.VERSION).getValue());
        target.setLicenseeAutoCreate(Boolean.parseBoolean(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Product.LICENSEE_AUTO_CREATE, Boolean.FALSE.toString()).getValue()));
        target.setDescription(SchemaFunction.propertyByName(source.getProperty(), Constants.Product.DESCRIPTION)
                .getValue());
        target.setLicensingInfo(SchemaFunction.propertyByName(source.getProperty(), Constants.Product.LICENSING_INFO)
                .getValue());

        for (final com.labs64.netlicensing.schema.context.List list : source.getList()) {
            if (Constants.DISCOUNT.equals(list.getName())) {
                final ProductDiscount productDiscount = new ProductDiscount();
                final Money price = convertPrice(list.getProperty(), Constants.Product.Discount.TOTAL_PRICE);
                productDiscount.setTotalPrice(price.getAmount());
                productDiscount.setCurrency(price.getCurrencyCode());
                if (SchemaFunction.propertyByName(list.getProperty(), Constants.Product.Discount.AMOUNT_FIX).getValue() != null) {
                    final Money amountFix = convertPrice(list.getProperty(), Constants.Product.Discount.AMOUNT_FIX);
                    productDiscount.setAmountFix(amountFix.getAmount());
                }
                final String amountPercent = SchemaFunction.propertyByName(list.getProperty(),
                        Constants.Product.Discount.AMOUNT_PERCENT).getValue();
                if (amountPercent != null) {
                    try {
                        productDiscount.setAmountPercent(DatatypeConverter.parseDecimal(amountPercent));
                    } catch (final NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Format for discount amount in percent is not correct, expected numeric format");
                    }
                }
                target.addDiscount(productDiscount);
            }
        }
        Collections.sort(target.getProductDiscounts());

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!ProductImpl.getReservedProps().contains(property.getName())) {
                target.addProperty(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Product newTarget() {
        return new ProductImpl();
    }

}
