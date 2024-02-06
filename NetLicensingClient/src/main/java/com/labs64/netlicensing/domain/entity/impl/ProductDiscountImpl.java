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
package com.labs64.netlicensing.domain.entity.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductDiscount;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.Money;

/**
 * Represents discount step as a discount amount (absolute or percentage) after total price reaches the given threshold.
 */
public class ProductDiscountImpl implements ProductDiscount, Serializable {

    private static final long serialVersionUID = -8665112497261365879L;

    private Product product;

    private BigDecimal totalPrice;

    private String currency;

    private BigDecimal amountFix;

    private BigDecimal amountPercent;

    @Override
    public void setProduct(final Product product) {
        this.product = product;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setTotalPrice(final BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public void setAmountFix(final BigDecimal amountFix) {
        this.amountFix = amountFix;
        amountPercent = null;
    }

    @Override
    public BigDecimal getAmountFix() {
        return amountFix;
    }

    @Override
    public void setAmountPercent(final BigDecimal amountPercent) {
        this.amountPercent = amountPercent;
        amountFix = null;
    }

    @Override
    public BigDecimal getAmountPercent() {
        return amountPercent;
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
                        "Format for discount amount in percent is not correct, expected numeric format.");
            }
        } else {
            // Dummy currency, Money.convertPrice is only used for the amount format check.
            final Money amountFix = Money.convertPrice(amount, Currency.EUR.value());
            setAmountFix(amountFix.getAmount());
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTotalPrice());
        builder.append(";");
        builder.append(getCurrency());
        builder.append(";");
        builder.append(getStringAmount());
        return builder.toString();
    }

    @Override
    public int compareTo(final ProductDiscount productDiscount) {
        return productDiscount.getTotalPrice().compareTo(totalPrice); // reverse order!
    }

}
