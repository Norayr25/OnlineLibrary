package com.Library.services;

import com.Library.exceptions.OutOfStockException;
import com.Library.exceptions.PaymentFailedException;
import com.Library.repositores.OrderRepository;
import com.Library.services.common.PaymentStatus;
import com.Library.services.entities.Cart;
import com.Library.services.entities.Order;
import com.Library.services.entities.User;
import com.Library.services.entities.Item;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link OrderService} interface responsible for handling order processing logic.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final UserManagementService userManagementService;

    public OrderServiceImpl(InventoryService inventoryService, PaymentService paymentService, OrderRepository orderRepository, UserManagementService userManagementService) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.userManagementService = userManagementService;
    }

    /**
     * Processes the order for the specified user and cart.
     *
     * @param user The user placing the order.
     * @param cart The cart containing the items to be purchased.
     * @throws OutOfStockException    If any of the items in the cart are out of stock.
     * @throws PaymentFailedException If the payment process fails.
     */
    @Transactional
    @Override
    public void processOrder(@Nonnull final User user,
                                            @Nonnull final Cart cart) {

        Map<Item, Integer> items = cart.getItems();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (!inventoryService.isAvailable(entry)) {
                throw new OutOfStockException("The library doesn't have that many books " + entry.getKey());
            }
        }
        int totalPrice = calculateTotal(items);
        PaymentStatus paymentStatus = paymentService.processPayment(user, totalPrice);

        if (paymentStatus == PaymentStatus.SUCCESS) {
            Order order = new Order(user.getId(), cart, totalPrice);
            orderRepository.save(order);

            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                inventoryService.updateInventory(entry);
            }
            user.setCart(null);
            userManagementService.saveUser(user);
        } else {
            throw new PaymentFailedException("Payment failed, insufficient funds.");
        }
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of orders placed by the specified user.
     */
    @Override
    public List<Order> getSpecificUserOrders(@Nonnull final Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return A list of all orders in the system.
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Calculates the total price of items in the cart.
     *
     * @param items The map containing items and their quantities.
     * @return The total price of items in the cart.
     */
    private int calculateTotal(@Nonnull final Map<Item, Integer> items) {
        int totalSum = 0;
        // Calculate total price of items in cart.
        for (Map.Entry<Item, Integer> item : items.entrySet()) {
            totalSum += item.getKey().getPrice() * item.getValue();
        }
        return totalSum;
    }
}
