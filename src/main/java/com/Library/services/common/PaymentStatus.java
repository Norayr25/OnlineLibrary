package com.Library.services.common;

/**
 * Enum representing the possible payment statuses.
 */
public enum PaymentStatus {
    /**
     * Payment was successful.
     */
    SUCCESS,

    /**
     * Payment failed.
     */
    FAILED,

    /**
     * Insufficient funds for the payment.
     */
    INSUFFICIENT_FUNDS
}