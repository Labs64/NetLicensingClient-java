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
package com.labs64.netlicensing.domain.entity.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Money;

/**
 * Represents discount step as a discount amount (absolute or percentage) after total price reaches the given threshold.
 */
public class ProductDiscount implements Comparable<ProductDiscount>, Serializable {

    private static final long serialVersionUID = -8665112497261365879L;

    private Product product;

    private BigDecimal totalPrice;

    private String currency;

    private BigDecimal amountFix;

    private BigDecimal amountPercent;

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountFix() {
        return amountFix;
    }

    public void setAmountFix(final BigDecimal amountFix) {
        this.amountFix = amountFix;
        amountPercent = null;
    }

    public BigDecimal getAmountPercent() {
        return amountPercent;
    }

    public void setAmountPercent(final BigDecimal amountPercent) {
        this.amountPercent = amountPercent;
        amountFix = null;
    }

    /**
     * Gets the discount amount as string, with '%' sign at the end indicating discount is given in percent.
     *
     * @return the string amount
     */
    public String getStringAmount() {
        if (getAmountFix() != null) {
            return String.valueOf(getAmountFix());
        }
        if (getAmountPercent() != null) {
            return String.valueOf(getAmountPercent()).concat("%");
        }
        return "Error";
    }

    /**
     * Sets the discount amount from string, '%' sign at the end indicates discount is provided in percent.
     *
     * @param amount
     *            discount amount as string
     */
    public void setStringAmount(final String amount) {
        if (amount.endsWith("%")) {
            try {
                setAmountPercent(DatatypeConverter.parseDecimal(amount.substring(0, amount.length() - 1)));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Format for discount amount in percent is not correct, expected numeric format");
            }
        } else {
            final Money amountFix = Money.convertPrice(amount, Currency.EUR.value());
            setAmountFix(amountFix.getAmount());
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Product Discount [");
        builder.append(Constants.Product.Discount.TOTAL_PRICE);
        builder.append("=");
        builder.append(getTotalPrice());
        builder.append(", ");
        builder.append(Constants.CURRENCY);
        builder.append("=");
        builder.append(getCurrency());
        if (getAmountFix() != null) {
            builder.append(", ");
            builder.append(Constants.Product.Discount.AMOUNT_FIX);
            builder.append("=");
            builder.append(getAmountFix());
        }
        if (getAmountPercent() != null) {
            builder.append(", ");
            builder.append(Constants.Product.Discount.AMOUNT_PERCENT);
            builder.append("=");
            builder.append(getAmountPercent());
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int compareTo(final ProductDiscount productDiscount) {
        return productDiscount.totalPrice.compareTo(totalPrice); // reverse order!
    }

}
