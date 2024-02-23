package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import com.labs64.netlicensing.domain.vo.Currency;

public interface Bundle extends BaseEntity {
    void setName(String name);
    
    String getName();
    
    void setPrice(BigDecimal price);

    BigDecimal getPrice();
    
    void setCurrency(Currency currency);
    
    Currency getCurrency();
    
    void setDescription(String description);
    
    String getDescription();

    void setLicenseTemplateNumbers(List<String> licenseTemplateNumbers);

    List<String> getLicenseTemplateNumbers();

    void addLicenseTemplateNumber(String licenseTemplateNumber);

    void removeLicenseTemplateNumber(String licenseTemplateNumber);
}
