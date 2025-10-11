package com.dallasdresses.exceptions.users;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String email) {
        super("User with email '" + email + "' already exists");
    }
}
