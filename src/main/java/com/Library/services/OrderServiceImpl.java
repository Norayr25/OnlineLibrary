package com.Library.services;

import com.Library.exceptions.OutOfStockException;
import com.Library.exceptions.PaymentFailedException;
import com.Library.repositores.OrderRepository;
import com.Library.services.common.PaymentStatus;
import com.Library.services.entities.Cart;
import com.Library.services.entities.Item;
import com.Library.services.entities.Order;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link OrderService} interface responsible for handling order processing logic.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = Logger.getLogger(OrderServiceImpl.class.getCanonicalName());
    private static final String LIBRARY_ERROR = "The library doesn't have that many books %s.";
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
     * @return The total price of items in the cart.
     * @throws OutOfStockException    If any of the items in the cart are out of stock.
     * @throws PaymentFailedException If the payment process fails.
     */
    @Transactional
    @Override
    public int processOrder(@Nonnull final User user,
                            @Nonnull final Cart cart) {

        Map<Item, Integer> items = cart.getItems();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (!inventoryService.isAvailable(entry)) {
                log.severe(String.format(LIBRARY_ERROR, entry.getKey()));
                throw new OutOfStockException(String.format(LIBRARY_ERROR, entry.getKey()));
            }
        }
        int totalPrice = calculateTotal(items);
        PaymentStatus paymentStatus = paymentService.processPayment(user, totalPrice);

        if (paymentStatus == PaymentStatus.SUCCESS) {
            Order order = new Order(user.getEmail(), cart, totalPrice);
            orderRepository.save(order);

            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                inventoryService.updateInventory(entry);
            }
            user.setCart(null);
            userManagementService.saveUser(user);
        } else {
            log.severe("Payment failed, insufficient funds.");
            throw new PaymentFailedException("Payment failed, insufficient funds.");
        }
        return totalPrice;
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param email The email of the user whose orders are to be retrieved.
     * @return A list of orders placed by the specified user.
     */
    @Override
    @Nonnull
    public List<Order> getSpecificUserOrders(@Nonnull final String email) {
        return orderRepository.findByUserEmail(email);
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return A list of all orders in the system.
     */
    @Override
    @Nonnull
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getTopUsersMadeOrder(Integer count) {
        List<Order> orderList = orderRepository.findAll();
        return orderList
                .stream()
                .collect(Collectors.groupingBy(Order::getUserEmail, Collectors.averagingDouble(Order::getTotalPrice)))
                .entrySet()
                .stream()
                .sorted((val1, val2) -> Double.compare(val2.getValue(), val1.getValue()))
                .limit(count)
                .map(entry -> new Order(entry.getKey(), null, entry.getValue()))
                .toList();
    }

    /**
     * Calculates the total price of items in the cart.
     *
     * @param items The map containing items and their quantities.
     * @return The total price of items in the cart.
     */
    private int calculateTotal(@Nonnull final Map<Item, Integer> items) {
        // Calculate total price of items in cart.
        return items.entrySet()
                .stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }
}
