package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;

public interface Country extends Comparable<Country> {

    void setCode(final String code);

    String getCode();

    void setName(final String name);

    String getName();

    void setVat(final BigDecimal vat);

    BigDecimal getVat();

    void setIsEu(final boolean isEu);

    boolean getIsEu();
}
