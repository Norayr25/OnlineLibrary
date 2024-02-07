package com.Library.repositores;

import com.Library.services.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Item entities.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
