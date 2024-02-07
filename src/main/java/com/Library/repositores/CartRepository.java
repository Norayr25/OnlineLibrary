package com.Library.repositores;

import com.Library.services.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Cart entities.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
