package com.Library.services.entities;

import com.Library.services.common.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Entity class representing a user in the library system.
 */
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
    private Integer postalZip;
    private String country;
    private String password;
    private String pan;
    private String expDate;
    private Integer cvv;
    // Adding new field to describe user's money, every user by default has some amount of dollars.
    private Integer money;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonIgnore
    private Cart cart;
}
