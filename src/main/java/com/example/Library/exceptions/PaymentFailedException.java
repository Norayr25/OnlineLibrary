package com.example.Library.exceptions;

public class PaymentFailedException extends RuntimeException{

    public PaymentFailedException(String message) {
        super(message);
    }
}
