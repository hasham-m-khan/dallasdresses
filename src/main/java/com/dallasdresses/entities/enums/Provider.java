package com.dallasdresses.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    GOOGLE("google"),
    MICROSOFT("microsoft"),
    EMAIL("email");

    private final String value;

    public static Provider fromValue(String value) {
        for (Provider provider : Provider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }
}
