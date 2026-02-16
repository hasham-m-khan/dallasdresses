package com.dallasdresses.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Hasher {
//    MD5,
//    SHA1,
//    SHA256
    BCRYPT("bcrypt"),
    ARGON2("argon2"),
    SCRYPT("scrypt");

    private final String value;

    public static Hasher fromValue(String value) {
        for (Hasher method : Hasher.values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown hash method: " + value);
    }
}
