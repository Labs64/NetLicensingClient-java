package com.labs64.netlicensing.domain.vo;

/**
 * NetLicensing supported currencies.
 */
public enum Currency {

    EUR("EUR");

    private final String value;

    /**
     * Instantiates a new currency.
     * 
     * @param currency
     *            currency value
     */
    Currency(final String currency) {
        value = currency;
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
     * Parse currency value to {@link Currency} enum.
     * 
     * @param value
     *            currency value
     * @return {@link Currency} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link Currency} enum object found
     */
    public static Currency parseValue(final String value) {
        for (Currency currency : Currency.values()) {
            if (currency.value.equalsIgnoreCase(value)) {
                return currency;
            }
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * Gets the enum safe.
     * 
     * @param value
     *            currency value
     * @return the enum safe
     */
    public static Currency parseValueSafe(final String value) {
        try {
            return parseValue(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
