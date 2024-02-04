package com.example.Library.repositores;

import com.example.Library.services.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);

    List<User> getByName(String name);

    User deleteByEmail(String email);
}
