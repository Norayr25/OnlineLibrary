package com.Library.repositores;

import com.Library.services.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
    List<User> getByName(String name);
}
