package com.labs64.netlicensing.domain.vo;

public enum VatMode {

    /**
     * VatMode: Gross
     */
    GROSS("GROSS"),

    /**
     * VatMode: Net
     */
    NET("NET");

    private final String value;

    /**
     * @param vatModeValue
     *            vatMode value
     */
    VatMode(final String vatModeValue) {
        value = vatModeValue;
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
     * Parse product VAT mode to {@link VatMode} enum.
     *
     * @param value
     *            vatMode value
     * @return {@link VatMode} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link VatMode} enum object found
     */
    public static VatMode parseValue(final String value) {
        for (final VatMode vatMode : VatMode.values()) {
            if (vatMode.value.equalsIgnoreCase(value)) {
                return vatMode;
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
    public static VatMode parseValueSafe(final String val) {
        try {
            return parseValue(val);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

}
