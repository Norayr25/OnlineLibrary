package com.example.Library.services;

import com.example.Library.exceptions.OutOfStockException;
import com.example.Library.exceptions.PaymentFailedException;
import com.example.Library.repositores.CartItemRepository;
import com.example.Library.repositores.CartRepository;
import com.example.Library.repositores.OrderRepository;
import com.example.Library.repositores.UsersRepository;
import com.example.Library.services.common.PaymentStatus;
import com.example.Library.services.entities.Cart;
import com.example.Library.services.entities.CartItem;
import com.example.Library.services.entities.Order;
import com.example.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final CartRepository cartRepository;

    private final UsersRepository usersRepository;


    private final CartItemRepository cartItemRepository;

    private final OrderRepository orderRepository;

    private final InventoryService inventoryService;

    private final PaymentService paymentService;

    @Autowired
    public PurchaseServiceImpl(CartRepository cartRepository, UsersRepository usersRepository,
                               CartItemRepository cartItemRepository,
                               OrderRepository orderRepository,
                               InventoryService inventoryService,
                               PaymentService paymentService) {
        this.cartRepository = cartRepository;
        this.usersRepository = usersRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    @Override
    public void addToCart(User user, CartItem item) {
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
        }
        cart.getItems().add(item);
        cartRepository.save(cart);
        cartItemRepository.save(item);

    }

    @Override
    public void removeFromCart(User user, CartItem item) {
        Cart cart = user.getCart();
        if (cart != null) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        }
    }

    @Override
    public void processOrder(User user) {
        String userEmail = user.getEmail();
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getItems();

        for (CartItem cartItem : cartItems) {
            if (!inventoryService.isAvailable(cartItem.getBook(), cartItem.getQuantity())) {
                throw new OutOfStockException("Book " + cartItem.getBook() + " is out of stock.");
            }
        }

        // Process payment.
        PaymentStatus paymentStatus = paymentService.processPayment(userEmail, calculateTotal(cartItems));

        if (paymentStatus == PaymentStatus.SUCCESS) {
            Order order = new Order(user, cart, calculateTotal(cartItems));
            orderRepository.save(order);

            for (CartItem cartItem : cartItems) {
                inventoryService.updateInventory(cartItem.getBook(), cartItem.getQuantity());
            }

//            cartRepository.clearCart(userId);
        } else {
            throw new PaymentFailedException("Payment failed.");
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserEmail(user.getEmail());
    }

    private double calculateTotal(List<CartItem> cartItems) {
        // Calculate total price of items in cart
        double total = 0.0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getBook().getPrice() * cartItem.getQuantity();
        }
        return total;
    }
}
