package com.example.Library.services.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalPrice;

    public Order(User user, Cart cart, double totalPrice) {
        this.user = user;
        this.cart = cart;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }
}