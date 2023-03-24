package com.labs64.netlicensing.domain.vo;

import java.util.Arrays;

public enum NotificationType {
    WEBHOOK;

    public static NotificationType parseString(final String value) {
        return Arrays.stream(NotificationType.values()).filter((t) -> t.name().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }
}
