package com.Library.services;

import com.Library.exceptions.UserNotFoundException;
import com.Library.repositores.UserRepository;
import com.Library.services.common.UserRole;
import com.Library.services.dtos.ChangeUserRoleDTO;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserManagementService {
    private static final Logger log = Logger.getLogger(UserManagementService.class.getCanonicalName());
    public static final String USER_NOT_FOUND = "User with specified email %s doesn't exist.";
    public static final String USER_ROLE_UPDATED = "User with specified email %s was updated with new role %s.";
    private final UserRepository usersRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository) {
        this.usersRepository = userRepository;
    }


    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user object if found, otherwise null.
     */
    @Nullable
    public User getUserByID(@Nonnull final Long id) {
        Optional<User> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    /**
     * Retrieves a user by their email.
     * @param email The email of the user to retrieve.
     * @return The user object if found, otherwise null.
     */
    @Nullable
    public User getUserByEmail(@Nonnull final String email) {
        return usersRepository.getByEmail(email);
    }

    /**
     * Retrieves a list of users by their name.
     * @param name The name of the users to retrieve.
     * @return A list of user objects matching the provided name.
     */
    @Nonnull
    public List<User> getUsersByName(@Nonnull final String name) {
        return usersRepository.getByName(name);
    }

    /**
     * Retrieves all users.
     * @return A list of all user objects.
     */
    @Nonnull
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    /**
     * Saves a user to the database.
     * @param user The user to save.
     * @return The saved user object.
     */
    @Nullable
    @Transactional
    public User saveUser(@Nonnull final User user) {
        validateUser(user);

        if (usersRepository.getByEmail(user.getEmail()) != null) {
            return null;
        }
        usersRepository.save(user);
        return user;
    }

    /**
     * Deletes a user from the database by their ID.
     * @param email The email of the user to delete.
     * @return The deleted user object.
     */
    @Nullable
    public User deleteUser(@Nonnull final String email) {
        User user = usersRepository.getByEmail(email);
        if (user == null) {
            return null;
        }
        usersRepository.delete(user);
        return user;
    }

    /**
     * Changes the role of a user.
     * @param changeUserRoleDTO DTO containing the user ID and the new role.
     * @return The updated user object.
     */
    @Nullable
    public User changeUserRole(@Nonnull final ChangeUserRoleDTO changeUserRoleDTO) {
        final String userRole = changeUserRoleDTO.userRole();
        final String userEmail = changeUserRoleDTO.userEmail();

        validateUserRoel(userRole);
        User user = usersRepository.getByEmail(userEmail);
        if (user != null) {
            usersRepository.updateUserRole(userEmail, UserRole.valueOf(userRole));
            log.info(String.format(USER_ROLE_UPDATED, userEmail, userRole));
            return user;
        } else {
            log.severe(String.format(USER_NOT_FOUND, userEmail));
            return null;
        }
    }

    /**
     * Sets the amount of money for a user.
     *
     * @param userId The ID of the user.
     * @param money  The amount of money to set.
     */
    public void setUserMoney(@Nonnull final Long userId, int money) {
        Optional<User> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User with specified id " + userId + " doesn't exist.");
        }
        User user = userOpt.get();
        user.setMoney(money);
        usersRepository.save(user);
    }

    public void setUserRole(@Nonnull final String email, @Nullable final UserRole userRole) {
        usersRepository.updateUserRole(email, userRole);
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

    private void validateUserRoel(@Nonnull final String userRole) {
        if (!UserRole.USER.toString().equalsIgnoreCase(userRole) &&
                !UserRole.ADMIN.toString().equalsIgnoreCase(userRole) &&
                !UserRole.SUPER_ADMIN.toString().equalsIgnoreCase(userRole)) {
            throw new RuntimeException("The specified role " + userRole + " not matched one of the roles(user, admin, super_admin).");
        }
    }
}
