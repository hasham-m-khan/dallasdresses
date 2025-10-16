package com.dallasdresses.exceptions.addresses;

public class AddressCreationException extends RuntimeException {

    public AddressCreationException(String message) {
        super(message);
    }

    public AddressCreationException() {
        super("Address creation failed.");
    }
}
