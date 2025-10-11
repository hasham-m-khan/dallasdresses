package com.dallasdresses.exceptions.users;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }

    public UserNotFoundException(String field, String value) {
        super("User with " + field + " '" + value + "' not found");
    }
}
