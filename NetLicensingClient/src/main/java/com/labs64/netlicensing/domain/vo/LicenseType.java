package com.labs64.netlicensing.domain.vo;

public enum LicenseType {

    /**
     * licenseType: feature.
     */
    FEATURE("FEATURE"),

    /**
     * licenseType: TimeVolume.
     */
    TIMEVOLUME("TIMEVOLUME");

    private final String value;

    /**
     * Instantiates a new license type.
     * 
     * @param licenseTypeValue
     *            licenseType value
     */
    LicenseType(final String licenseTypeValue) {
        value = licenseTypeValue;
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
     * Parse license type value to {@link LicenseType} enum.
     * 
     * @param value
     *            licenseType value
     * @return {@link LicenseType} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link LicenseType} enum object found
     */
    public static LicenseType parseValue(final String value) {
        for (LicenseType licenseType : LicenseType.values()) {
            if (licenseType.value.equalsIgnoreCase(value)) {
                return licenseType;
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
    public static LicenseType parseValueSafe(final String val) {
        try {
            return parseValue(val);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
