package com.labs64.netlicensing.schema.converter;

import java.util.Collections;

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductDiscount;
import com.labs64.netlicensing.domain.vo.Money;
import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Product} object.
 */
public class ItemToProductConverter extends ItemToEntityBaseConverter<Product> {

    @Override
    public Product convert(final Item source) {
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
            if (!Product.getReservedProps().contains(property.getName())) {
                target.getProductProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Product newTarget() {
        return new Product();
    }

}