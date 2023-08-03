package com.labs64.netlicensing.domain.vo;

import java.util.Arrays;

public enum Event {
    CREATE_LICENSEE, 
    CREATE_LICENSE,
    WARNING_LEVEL_TO_ANY,
    WARNING_LEVEL_TO_GREEN,
    WARNING_LEVEL_TO_YELLOW,
    WARNING_LEVEL_TO_RED;

    public static Event parseString(final String value) {
        return Arrays.stream(Event.values()).filter((e) -> e.name().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
