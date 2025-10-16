package com.dallasdresses.exceptions.addresses;

import java.util.NoSuchElementException;

public class AddressNotFoundException extends NoSuchElementException {

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(Long id) {
        super("Could not find address with id: " + id);
    }
}
