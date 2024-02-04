package com.example.Library.services.entities;

import com.example.Library.services.dtos.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    // Every user has USER role by default.
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
}
