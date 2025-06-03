package com.labs64.netlicensing.domain.vo;

public enum DiscountType {
    FIX,

    PERCENT;

    public static DiscountType parseString(final String discountType) {
        if (discountType != null) {
            for (final DiscountType type : DiscountType.values()) {
                if (discountType.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException(discountType);
    }
}
