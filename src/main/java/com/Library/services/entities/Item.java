package com.Library.services.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Entity class representing an item in the library system.
 */
@Setter
@Getter
@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class Item {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The field describes the price of an item, every item by default will have some price.
    private int price;

    // The field describes the count of an items, by default we will have some count with the same type.
    private int quantity;
}
