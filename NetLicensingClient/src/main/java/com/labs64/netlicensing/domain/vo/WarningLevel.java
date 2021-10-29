package com.labs64.netlicensing.domain.vo;

public enum WarningLevel {
    GREEN,
    YELLOW,
    RED;
    
    public static WarningLevel parseString(final String warningLevelStr) {
        if (warningLevelStr != null) {
            for (final WarningLevel warningLevel : WarningLevel.values()) {
                if (warningLevelStr.equals(warningLevel.name())) {
                    return warningLevel;
                }
            }
        }
        return null;
    }

}
