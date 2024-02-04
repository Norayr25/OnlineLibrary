package com.example.Library.services.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Entity
@Table(name = "user_permissions")
public class Permission {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Define many-to-one association with User entity
    @ManyToOne
    @JoinColumn(name = "email")
    private User user;
}
