package com.labs64.netlicensing.domain.vo;

import java.math.BigDecimal;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;

/**
 * Holds amount of money with associated currency.
 * <p>
 * Consider more comprehensive implementation using one of:
 * <ul>
 * <li>http://joda-money.sourceforge.net/</li>
 * <li>http://java.net/projects/javamoney</li>
 * </ul>
 */
public class Money {

    private BigDecimal amount;

    private String currencyCode;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public static Money convertPrice(final String rawPrice, final String rawCurrency) {
        final Money target = new Money();
        if (StringUtils.isNotBlank(rawPrice)) {
            try {
                target.setAmount(DatatypeConverter.parseDecimal(rawPrice));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("'" + Constants.PRICE
                        + "' format is not correct, expected '0.00' format");
            }
            if (StringUtils.isNotBlank(rawCurrency)) {
                if (Currency.parseValueSafe(rawCurrency) == null) {
                    throw new IllegalArgumentException("Unsupported currency!");
                }
                target.setCurrencyCode(rawCurrency);
            } else {
                throw new IllegalArgumentException("'" + Constants.PRICE + "' field must be accompanied with the '"
                        + Constants.CURRENCY + "' field");
            }
        } else { // 'price' is not provided
            if (StringUtils.isNotBlank(rawCurrency)) {
                throw new IllegalArgumentException("'" + Constants.CURRENCY + "' field can not be used without the '"
                        + Constants.PRICE + "' field");
            }
        }
        return target;
    }

}
