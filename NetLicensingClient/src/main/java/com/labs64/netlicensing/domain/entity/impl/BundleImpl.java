package com.labs64.netlicensing.domain.entity.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Bundle;
import com.labs64.netlicensing.domain.vo.Currency;

public class BundleImpl extends BaseEntityImpl implements Bundle {
    private String name;

    private String description;

    private BigDecimal price;

    private Currency currency;

    private List<String> licenseTemplatesNumbers;

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setLicenseTemplatesNumbers(final List<String> licenseTemplatesNumbers) {
        this.licenseTemplatesNumbers = licenseTemplatesNumbers;
    }

    @Override
    public List<String> getLicenseTemplatesNumbers() {
        return licenseTemplatesNumbers;
    }

    @Override
    public void addLicenseTemplateNumber(final String licenseTemplateNumber) {
        if (getLicenseTemplatesNumbers() == null) {
            this.licenseTemplatesNumbers = new ArrayList<>();
        }

        this.licenseTemplatesNumbers.add(licenseTemplateNumber);
    }

    @Override
    public void removeLicenseTemplateNumber(final String licenseTemplateNumber) {
        if (getLicenseTemplatesNumbers() == null) {
            return;
        }

        getLicenseTemplatesNumbers().remove(licenseTemplateNumber);
    }

    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.Bundle.DESCRIPTION);
        reserved.add(Constants.PRICE);
        reserved.add(Constants.CURRENCY);
        reserved.add(Constants.Bundle.LICENSE_TEMPLATES_NUMBERS);
        return reserved;
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.NAME, getName());

        if (getDescription() != null) {
            map.add(Constants.Bundle.DESCRIPTION, getDescription());
        }

        map.add(Constants.PRICE, getPrice());
        map.add(Constants.CURRENCY, getCurrency());

        if (getLicenseTemplatesNumbers() != null) {
            map.add(Constants.Bundle.LICENSE_TEMPLATES_NUMBERS, String.join(",", getLicenseTemplatesNumbers()));
        }

        return map;
    }
}
