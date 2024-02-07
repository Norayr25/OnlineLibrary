package com.Library.exceptions;

/**
 * Exception indicating that an item is out of stock.
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
