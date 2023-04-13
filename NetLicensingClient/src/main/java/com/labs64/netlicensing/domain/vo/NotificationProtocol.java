package com.labs64.netlicensing.domain.vo;

import java.util.Arrays;

public enum NotificationProtocol {
    WEBHOOK;

    public static NotificationProtocol parseString(final String value) {
        return Arrays.stream(NotificationProtocol.values()).filter((t) -> t.name().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }
}
