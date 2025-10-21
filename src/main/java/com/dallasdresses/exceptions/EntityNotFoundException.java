package com.dallasdresses.exceptions;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entity, Long id) {
        super(entity + " not found with id: " + id);
    }

    public EntityNotFoundException(String entity, String field, String value) {
        super(entity + " not found with " + field + " '" + value + "'");
    }
}
