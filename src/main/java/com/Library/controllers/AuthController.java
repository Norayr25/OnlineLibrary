package com.Library.controllers;

import com.Library.services.AuthService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;

/**
 * Controller for handling user authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Signs a user into the library system.
     *
     * @return A ResponseDTO containing the signed-in user or an error message if sign-in fails.
     */
    @PostMapping("/signIn")
    public ResponseDTO<User> signInToLibrary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        try {
            User signedUser = authService.signInToLibrary(userEmail);
            if (signedUser == null) {
                return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND, null);
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), "The user has successfully signed in and has been assigned the USER role.", signedUser);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Failed to sign in user. " + e.getMessage(), null);
        }
    }

    /**
     * Registers a new user into the library system.
     *
     * @param user The user to register.
     * @return A ResponseDTO containing the newly registered user or an error message if registration fails.
     */
    @PostMapping("/signUp")
    public ResponseDTO<User> signUpToLibrary(@RequestBody User user) {
        try {
            User newUser = authService.signUpToLibrary(user);
            if (newUser == null) {
                return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(),
                        "User with an email " + user.getEmail() + " already exist.", null);
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), "User signed up successfully.", newUser);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Failed to sign up user. " + e.getMessage(), null);
        }
    }

    /**
     * Signs a user out from the library system.
     *
     * @return A ResponseDTO containing the signed-out user or an error message if sign-out fails.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/signOut")
    public ResponseDTO<User> signOutFromLibrary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User existingUser = authService.signOutToLibrary(userEmail);

        if (existingUser == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND, null);
        }

        return new ResponseDTO<>(HttpStatus.OK.value(),
                "User with an email " + userEmail + " successfully signed out.", existingUser);
    }
}
