package com.Library.exceptions;

/**
 * Exception indicating that payment was failed.
 */
public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String message) {
        super(message);
    }
}
