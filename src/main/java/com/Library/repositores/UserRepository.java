package com.Library.repositores;

import com.Library.services.common.UserRole;
import com.Library.services.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
    List<User> getByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userRole = :userRole WHERE u.email = :email")
    void updateUserRole(@Param("email") String email, @Param("userRole") UserRole userRole);
}
