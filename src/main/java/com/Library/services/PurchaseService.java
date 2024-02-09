package com.Library.services;

import com.Library.exceptions.CartNotFoundException;
import com.Library.exceptions.UserNotFoundException;
import com.Library.services.entities.Cart;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;

/**
 * Service class responsible for handling purchases.
 */
@Service
public class PurchaseService {
    private static final Logger log = Logger.getLogger(PurchaseService.class.getCanonicalName());
    public static final String CART_NOT_FOUND_OR_EMPTY = "Cart not found or empty for the user %s.";
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
     * @param email The email of the user initiating the purchase.
     * @return The total price of items in the cart.
     * @throws UserNotFoundException    If the user with the specified ID is not found.
     * @throws CartNotFoundException    If the cart for the user is not found or empty.
     */
    public int purchaseItems(@Nonnull final String email) {
        User user = userManagementService.getUserByEmail(email);

        if (user == null) {
            log.severe(String.format(USER_NOT_FOUND, email));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, email));
        }

        Cart cart = user.getCart();
        if (cart == null) {
            log.severe(String.format(CART_NOT_FOUND_OR_EMPTY, email));
            throw new CartNotFoundException(String.format(CART_NOT_FOUND_OR_EMPTY, email));
        }

        return orderService.processOrder(user, cart);
    }
}
