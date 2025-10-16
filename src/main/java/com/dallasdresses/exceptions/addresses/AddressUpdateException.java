package com.dallasdresses.exceptions.addresses;

public class AddressUpdateException extends RuntimeException {

    public AddressUpdateException(String message) {
        super(message);
    }

    public AddressUpdateException() {
        super("Error updating address");
    }
}
