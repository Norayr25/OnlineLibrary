package com.example.Library.services;

import com.example.Library.repositores.UsersRepository;
import com.example.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserManagementService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public User getUserByID(@Nonnull final Long id) {
        Optional<User> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByEmail(@Nonnull final String email) {
        User user = usersRepository.getByEmail(email);
        user.setPassword(null);
        return user;
    }

    public List<User> getUsersByName(@Nonnull final String name) {
        List<User> usersList = usersRepository.getByName(name);
        return usersList;
    }

    @Nonnull
    public List<User> getAllUsers() {
        List<User> userDTOList = usersRepository.findAll();
        return userDTOList;
    }

    public String createUser(@Nonnull final User user) {
        validateUser(user);

        if (usersRepository.getByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with the " + user.getEmail() + " email already exists.");
        }
        usersRepository.save(user);
        return "AA";
    }

    public void deleteUser(@Nonnull final Long id) {
        usersRepository.deleteById(id);
    }

    private void validateUser(@Nonnull final User user) {
        if (user.getEmail() == null ||
                user.getPassword() == null || user.getName() == null) {
            throw new RuntimeException("One of the mandatory fields(name, password, email) are not provided.");
        }
        if (!user.getEmail().contains(".com") && !user.getEmail().contains("@")) {
            throw new RuntimeException("The email " + user.getEmail() + " format is incorrect.");
        }
    }
}
