package com.Library.exceptions;

/**
 * Exception indicating that cart not found.
 */
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
