package com.labs64.netlicensing.domain.vo;

import java.util.Arrays;

public enum Event {
    LICENSEE_CREATED,
    LICENSE_CREATED,
    WARNING_LEVEL_CHANGED;

    public static Event parseString(final String value) {
        return Arrays.stream(Event.values()).filter((e) -> e.name().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
