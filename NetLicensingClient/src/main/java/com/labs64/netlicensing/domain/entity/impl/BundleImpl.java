package com.labs64.netlicensing.domain.entity.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Bundle;
import com.labs64.netlicensing.domain.vo.Currency;

public class BundleImpl extends BaseEntityImpl implements Bundle {

    private static final long serialVersionUID = -1666422065511383898L;

    private String name;

    private String description;

    private BigDecimal price;

    private Currency currency;

    private List<String> licenseTemplateNumbers;

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
    public void setLicenseTemplateNumbers(final List<String> licenseTemplateNumbers) {
        this.licenseTemplateNumbers = licenseTemplateNumbers;
    }

    @Override
    public List<String> getLicenseTemplateNumbers() {
        return licenseTemplateNumbers;
    }

    @Override
    public void addLicenseTemplateNumber(final String licenseTemplateNumber) {
        if (getLicenseTemplateNumbers() == null) {
            this.licenseTemplateNumbers = new ArrayList<>();
        }

        this.licenseTemplateNumbers.add(licenseTemplateNumber);
    }

    @Override
    public void removeLicenseTemplateNumber(final String licenseTemplateNumber) {
        if (getLicenseTemplateNumbers() == null) {
            return;
        }

        getLicenseTemplateNumbers().remove(licenseTemplateNumber);
    }

    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.NAME);
        reserved.add(Constants.Bundle.DESCRIPTION);
        reserved.add(Constants.PRICE);
        reserved.add(Constants.CURRENCY);
        reserved.add(Constants.Bundle.LICENSE_TEMPLATE_NUMBERS);
        return reserved;
    }

    @Override
    public Map<String, Object> asMap() {
        final Map<String, Object> map = super.asMap();
        map.put(Constants.NAME, getName());

        if (getDescription() != null) {
            map.put(Constants.Bundle.DESCRIPTION, getDescription());
        }

        map.put(Constants.PRICE, getPrice());
        map.put(Constants.CURRENCY, getCurrency());

        if (getLicenseTemplateNumbers() != null) {
            map.put(Constants.Bundle.LICENSE_TEMPLATE_NUMBERS, String.join(",", getLicenseTemplateNumbers()));
        }

        return map;
    }
}
