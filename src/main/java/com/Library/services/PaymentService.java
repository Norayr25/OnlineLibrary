package com.Library.services;

import com.Library.services.common.PaymentStatus;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for processing payments.
 */
@Service
public class PaymentService {
    private final UserManagementService userManagementService;

    public PaymentService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Processes a payment for the specified user.
     *
     * @param user        The user making the payment.
     * @param totalAmount The total amount to be paid.
     * @return The payment status indicating whether the payment was successful or failed due to insufficient funds.
     */
    @Nonnull
    public PaymentStatus processPayment(@Nonnull final User user, int totalAmount) {
        if (user.getMoney() >= totalAmount) {
            userManagementService.setUserMoney(user.getId(), user.getMoney() - totalAmount);
            return PaymentStatus.SUCCESS;
        }
        return PaymentStatus.INSUFFICIENT_FUNDS;
    }
}