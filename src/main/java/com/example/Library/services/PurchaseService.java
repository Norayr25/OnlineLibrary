package com.example.Library.services;

import com.example.Library.services.entities.CartItem;
import com.example.Library.services.entities.Order;
import com.example.Library.services.entities.User;

import java.util.List;

// PurchaseService interface
public interface PurchaseService {
    void addToCart(User user, CartItem item);

    void removeFromCart(User user, CartItem item);

    void processOrder(User user);

    List<Order> getOrderHistory(User user);
}