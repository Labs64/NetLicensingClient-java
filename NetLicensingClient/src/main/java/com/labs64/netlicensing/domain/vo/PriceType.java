package com.labs64.netlicensing.domain.vo;

public enum PriceType {

    /**
     * ProductPrice: brutto.
     */
    BRUTTO("BRUTTO"),

    /**
     * ProductPrice: netto.
     */
    NETTO("NETTO");

    private final String value;

    /**
     * Instantiates a new license type.
     *
     * @param priceTypeValue
     *            priceType value
     */
    PriceType(final String priceTypeValue) {
        value = priceTypeValue;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    public String value() {
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Parse product price type to {@link PriceType} enum.
     *
     * @param value
     *            priceType value
     * @return {@link PriceType} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link PriceType} enum object found
     */
    public static PriceType parseValue(final String value) {
        for (final PriceType priceType : PriceType.values()) {
            if (priceType.value.equalsIgnoreCase(value)) {
                return priceType;
            }
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * Gets the enum safe.
     *
     * @param val
     *            the val
     * @return the enum safe
     */
    public static PriceType parseValueSafe(final String val) {
        try {
            return parseValue(val);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

}
