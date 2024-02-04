package com.example.Library.repositores;

import com.example.Library.services.entities.Cart;
import com.example.Library.services.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
