package com.Library.services.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity class representing a cart in the library system.
 */
@Setter
@Getter
@Entity
@Table(name = "carts")
public class Cart {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Using a map to store items and their quantities in the cart
    @ElementCollection
    private Map<Item, Integer> items = new HashMap<>();

    public void addItem(Item item, int quantity) {
        items.put(item, quantity);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public boolean containsItem(Item item) {
        return items.containsKey(item);
    }
}
