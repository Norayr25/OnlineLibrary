package com.Library.services.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    private String userEmail;

    private double totalPrice;

    public Order(String userEmail, Cart cart, double totalPrice) {
        this.userEmail = userEmail;
        this.cart = cart;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }
}