package com.Library.services;

import com.Library.services.common.UserRole;
import com.Library.services.entities.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;

/**
 * Service class responsible for handling authentication-related operations in the library system.
 */
@Service
public class AuthService {
    private static final Logger log = Logger.getLogger(AuthService.class.getCanonicalName());
    private final UserManagementService userManagementService;

    public AuthService(UserManagementService userManagementService) {

        this.userManagementService = userManagementService;
    }

    /**
     * Attempts to sign in a user with the provided email.
     *
     * @param email The email of the user to sign in.
     * @return The signed-in user if authentication succeeds, or null if the user does not exist.
     */
    @Nullable
    public User signInToLibrary(@Nonnull final String email) {
        User existingUser = userManagementService.getUserByEmail(email);

        if (existingUser == null) {
            log.severe(String.format(USER_NOT_FOUND, email));
            return null;
        }
        userManagementService.setUserRole(existingUser.getEmail(), UserRole.USER);
        return existingUser;
    }

    /**
     * Registers a new user in the library system.
     *
     * @param user The user to sign up.
     * @return The newly signed-up user, or null if a user with the same email already exists.
     */
    @Nullable
    public User signUpToLibrary(@Nonnull final User user) {
        User existingUser = userManagementService.getUserByEmail(user.getEmail());

        if (existingUser != null) {
            return null;
        }

        userManagementService.saveUser(user);
        return user;
    }

    /**
     * Signs out a user with the provided email.
     *
     * @param email The email of the user to sign out.
     * @return The signed-out user, or null if the user does not exist.
     */
    @Nullable
    public User signOutToLibrary(@Nonnull final String email) {
        User existingUser = userManagementService.getUserByEmail(email);

        if (existingUser == null) {
            log.severe(String.format(USER_NOT_FOUND, email));
            return null;
        }

        userManagementService.setUserRole(email, null);
        return existingUser;
    }
}
