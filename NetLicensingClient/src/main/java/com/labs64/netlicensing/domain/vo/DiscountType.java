package com.labs64.netlicensing.domain.vo;

public enum DiscountType {
    FIX,

    PERCENT;

    public static DiscountType parseValue(final String discountType) {
        if (discountType != null) {
            for (final DiscountType type : DiscountType.values()) {
                if (discountType.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException(discountType);
    }

    public static DiscountType parseValueSafe(final String discountType) {
        try {
            return parseValue(discountType);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
