package com.example.Library.services.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Entity
public class CartItem {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    private int quantity;

    public CartItem(Book book) {
        this.book = book;
        this.quantity = 1;
    }

    public void increment() {
        ++quantity;
    }

    public CartItem() {

    }
}