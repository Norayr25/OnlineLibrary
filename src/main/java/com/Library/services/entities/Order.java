package com.Library.services.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Entity class representing an order in the library system.
 */
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Long userId;

    private double totalPrice;

    public Order(Long userId, Cart cart, double totalPrice) {
        this.userId = userId;
        this.cart = cart;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }
}