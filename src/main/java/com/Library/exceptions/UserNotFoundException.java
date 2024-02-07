package com.Library.exceptions;
/**
 * Exception indicating that a user was not found.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}