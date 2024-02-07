package com.Library.services;

import com.Library.repositores.CartRepository;
import com.Library.repositores.ItemRepository;
import com.Library.services.dtos.CartItemDTO;
import com.Library.services.entities.Cart;
import com.Library.services.entities.User;
import com.Library.services.entities.Item;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for managing cart-related operations.
 */
@Service
public class CartService {
    private static final Logger logger = Logger.getLogger(CartService.class.getCanonicalName());
    public static final String EMPTY_CART = "Cart is empty for the user with ID: %s.";
    public static final String ITEM_NOT_FOUND_IN_CART = "The cart doesn't contain an item with ID %s.";
    public static final String INSUFFICIENT_ITEM_COUNT_IN_CART = "There are no %s items in the cart with item ID %s.";
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
     * @param cartItemDTO The DTO containing information about the user and item to be added.
     * @return The added item.
     * @throws IllegalArgumentException If the user or item is not found.
     */
    @Nonnull
    public Item addItemToCart(@Nonnull final CartItemDTO cartItemDTO) {
        Long userId = cartItemDTO.getUserId();
        Long itemId = cartItemDTO.getItemId();
        int count = cartItemDTO.getCount() == null ? 1 : cartItemDTO.getCount();

        User user = userManagementService.getUserByID(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
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
            Integer oldCount = cart.getItems().get(item);
            cart.getItems().put(item, oldCount + count);
        } else {
            cart.addItem(item, count);
        }
        cartRepository.save(cart);
        return item;
    }

    /**
     * Removes an item from the user's cart.
     *
     * @param cartItemDTO The DTO containing information about the user and item to be removed.
     * @return The removed item.
     * @throws IllegalArgumentException If the user or item is not found, or if the item is not present in the cart.
     */
    @Nonnull
    public Item removeItemFromCart(@Nonnull final CartItemDTO cartItemDTO) {
        Long userId = cartItemDTO.getUserId();
        Long itemId = cartItemDTO.getItemId();
        int count = cartItemDTO.getCount() == null ? 1 : cartItemDTO.getCount();

        User user = userManagementService.getUserByID(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }

        Cart cart = user.getCart();
        Item item = itemOpt.get();
        if (cart != null && !cart.getItems().isEmpty()) {
            if (!cart.containsItem(item)) {
                logger.severe(String.format(ITEM_NOT_FOUND_IN_CART, itemId));
                throw new IllegalArgumentException(String.format(ITEM_NOT_FOUND_IN_CART, itemId));
            }
            Integer itemCount = cart.getItems().get(item);
            if (itemCount < count) {
                logger.severe(String.format(INSUFFICIENT_ITEM_COUNT_IN_CART, count, itemId));
                throw new IllegalArgumentException(String.format(INSUFFICIENT_ITEM_COUNT_IN_CART, count, itemId));
            } else if (itemCount == count) {
                // In this case item just removed from cart.
                cart.removeItem(item);
            } else {
                // Decreasing item count in the cart.
                cart.getItems().put(item, itemCount - count);
            }
            cartRepository.save(cart);
        } else {
            logger.severe(String.format(EMPTY_CART, userId));
            throw new IllegalArgumentException(String.format(EMPTY_CART, userId));
        }

        return item;
    }
}
