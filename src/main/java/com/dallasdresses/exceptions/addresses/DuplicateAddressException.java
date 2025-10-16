package com.dallasdresses.exceptions.addresses;

public class DuplicateAddressException extends RuntimeException {

    public DuplicateAddressException(String message) {
        super(message);
    }

    public DuplicateAddressException() {
        super("Address already exists for user");
    }
}
