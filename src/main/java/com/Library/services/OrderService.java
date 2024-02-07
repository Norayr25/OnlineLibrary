package com.Library.services;

import com.Library.services.entities.Cart;
import com.Library.services.entities.Order;
import com.Library.services.entities.User;

import java.util.List;

/**
 * Interface defining operations related to order processing.
 */
public interface OrderService {
    void processOrder(User user, Cart cart);

    List<Order> getSpecificUserOrders(Long userId);

    List<Order> getAllOrders();
}
