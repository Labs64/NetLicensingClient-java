package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;
import java.util.List;

public interface Country extends Comparable<Country> {

    List<Transaction> getTransactions();

    void addTransaction(Transaction transaction);

    void setCode(final String code);

    String getCode();

    void setName(final String name);

    String getName();

    void setVat(final BigDecimal vat);

    BigDecimal getVat();

    void setIsEu(final boolean isEu);

    boolean getIsEu();
}
