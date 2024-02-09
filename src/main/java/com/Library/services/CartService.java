package com.Library.services;

import com.Library.exceptions.CartNotFoundException;
import com.Library.exceptions.PaymentFailedException;
import com.Library.exceptions.UserNotFoundException;
import com.Library.repositores.CartRepository;
import com.Library.repositores.ItemRepository;
import com.Library.services.entities.Cart;
import com.Library.services.entities.Item;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;

/**
 * Service class for managing cart-related operations.
 */
@Service
public class CartService {
    private static final Logger logger = Logger.getLogger(CartService.class.getCanonicalName());
    public static final String EMPTY_CART = "Cart is empty for the user with email: %s.";
    public static final String ITEM_NOT_FOUND_IN_CART = "The cart doesn't contain an item with email %s.";
    public static final String INSUFFICIENT_ITEM_COUNT_IN_CART = "There are no %s items in the cart with item email %s.";
    private final UserManagementService userManagementService;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CartService(UserManagementService userManagementService,
                       CartRepository cartRepository,
                       ItemRepository itemRepository) {
        this.userManagementService = userManagementService;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Adds an item to the user's cart.
     *
     * @param userEmail The email of the user.
     * @param itemId    The ID of the item to be added.
     * @param count     The number of items to add. If null, defaults to 1.
     * @return The item that was added to the cart.
     * @throws UserNotFoundException      If the user with the specified email is not found.
     * @throws IllegalArgumentException   If the item with the specified ID is not found,
     *                                    or if the count is less than or equal to 0.
     */
    @Nonnull
    public Item addItemToCart(@Nonnull final String userEmail,
                              @Nonnull final Long itemId,
                              @Nullable final Integer count) {
        int itemsCount = count == null ? 1 : count;

        User user = userManagementService.getUserByEmail(userEmail);
        if (user == null) {
            logger.severe(String.format(USER_NOT_FOUND, userEmail));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userEmail));
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            logger.severe("Item not found with ID: " + itemId);
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
        }
        Item item = itemOpt.get();
        if (cart.containsItem(item)) {
            // If cart already contains the item, then just updating the quantity.
            Integer currentCount = cart.getItems().get(item);
            cart.getItems().put(item, currentCount + itemsCount);
        } else {
            cart.addItem(item, itemsCount);
        }
        cartRepository.save(cart);
        return item;
    }

    /**
     * Removes an item from the user's cart.
     *
     * @param userEmail The email of the user.
     * @param itemId    The ID of the item to be removed.
     * @param count     The number of items to remove. If null, defaults to 1.
     * @return The item that was removed from the cart.
     * @throws UserNotFoundException        If the user with the specified email is not found.
     * @throws IllegalArgumentException     If the item with the specified ID is not found,
     *                                      or if the count is less than or equal to 0,
     *                                      or if the item is not found in the user's cart,
     *                                      or if there are insufficient items of the specified count in the cart.
     * @throws CartNotFoundException        If the user's cart is empty.
     * @throws PaymentFailedException       If there are insufficient items of the specified count in the cart.
     */
    @Nonnull
    public Item removeItemFromCart(@Nonnull final String userEmail,
                                   @Nonnull final Long itemId,
                                   @Nullable final Integer count) {
        int itemsCount = count == null ? 1 : count;

        User user = userManagementService.getUserByEmail(userEmail);
        if (user == null) {
            logger.severe(String.format(USER_NOT_FOUND, userEmail));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userEmail));
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            logger.severe("Item not found with ID: " + itemId);
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }

        Cart cart = user.getCart();
        Item item = itemOpt.get();
        if (cart != null && !cart.getItems().isEmpty()) {
            if (!cart.containsItem(item)) {
                logger.severe(String.format(ITEM_NOT_FOUND_IN_CART, itemId));
                throw new IllegalArgumentException(String.format(ITEM_NOT_FOUND_IN_CART, itemId));
            }
            Integer currentCount = cart.getItems().get(item);
            if (currentCount < itemsCount) {
                logger.severe(String.format(INSUFFICIENT_ITEM_COUNT_IN_CART, itemsCount, itemId));
                throw new PaymentFailedException(String.format(INSUFFICIENT_ITEM_COUNT_IN_CART, itemsCount, itemId));
            } else if (currentCount == itemsCount) {
                // In this case item just removed from cart.
                cart.removeItem(item);
            } else {
                // Decreasing item count in the cart.
                cart.getItems().put(item, currentCount - itemsCount);
            }
            cartRepository.save(cart);
        } else {
            logger.severe(String.format(EMPTY_CART, userEmail));
            throw new CartNotFoundException(String.format(EMPTY_CART, userEmail));
        }

        return item;
    }
}
