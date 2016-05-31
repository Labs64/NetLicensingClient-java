package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;

public interface ProductDiscount extends Comparable<ProductDiscount> {

    void setProduct(final Product product);

    Product getProduct();

    void setTotalPrice(final BigDecimal totalPrice);

    BigDecimal getTotalPrice();

    void setCurrency(final String currency);

    String getCurrency();

    void setAmountFix(final BigDecimal amountFix);

    BigDecimal getAmountFix();

    void setAmountPercent(final BigDecimal amountPercent);

    BigDecimal getAmountPercent();

}
