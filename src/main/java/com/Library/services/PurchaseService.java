package com.Library.services;

import com.Library.exceptions.CartNotFoundException;
import com.Library.exceptions.UserNotFoundException;
import com.Library.services.entities.Cart;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling purchases.
 */
@Service
public class PurchaseService {
    public static final String CART_NOT_FOUND_OR_EMPTY = "Cart not found or empty for the user %s.";
    public static final String USER_NOT_FOUND = "User with an identifier %s not found.";
    private final OrderService orderService;
    private final UserManagementService userManagementService;
    @Autowired
    public PurchaseService(OrderService orderService,
                           UserManagementService userManagementService) {
        this.orderService = orderService;
        this.userManagementService = userManagementService;
    }

    /**
     * Initiates the purchase process for the specified user.
     *
     * @param userId The identifier of the user initiating the purchase.
     * @throws UserNotFoundException    If the user with the specified ID is not found.
     * @throws CartNotFoundException    If the cart for the user is not found or empty.
     */
    public void purchaseItems(@Nonnull final Long userId) {
        User user = userManagementService.getUserByID(userId);

        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
        }

        Cart cart = user.getCart();
        if (cart == null) {
            throw new CartNotFoundException(String.format(CART_NOT_FOUND_OR_EMPTY, userId));
        }

        orderService.processOrder(user, cart);
    }
}
