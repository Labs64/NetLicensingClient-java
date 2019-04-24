package com.labs64.netlicensing.domain.vo;

public enum LicenseeSecretMode {

    /**
     * @deprecated
     */
    @Deprecated DISABLED("DISABLED"),

    PREDEFINED("PREDEFINED"),

    CLIENT("CLIENT");

    private final String value;

    /**
     * Instantiates a new Licensee Secret Mode.
     *
     * @param licenseeSecretModeValue
     *            LicenseeSecretMode value
     */
    LicenseeSecretMode(final String licenseeSecretModeValue) {
        value = licenseeSecretModeValue;
    }

    public static LicenseeSecretMode parseString(final String value) {
        for (final LicenseeSecretMode licenseeSecretMode : LicenseeSecretMode.values()) {
            if (licenseeSecretMode.name().equalsIgnoreCase(value)) {
                return licenseeSecretMode;
            }
        }
        return LicenseeSecretMode.PREDEFINED;
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
}
