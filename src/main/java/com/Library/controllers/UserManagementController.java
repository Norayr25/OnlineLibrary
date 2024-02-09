package com.Library.controllers;

import com.Library.services.UserManagementService;
import com.Library.services.dtos.ChangeUserRoleDTO;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;
import static com.Library.services.UserManagementService.USER_ROLE_UPDATED;

/**
 * Controller for handling user management operations.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserManagementController {
    private final UserManagementService userManagementService;

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return A ResponseDTO containing the retrieved user or an error message if the user is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseDTO<User> getUserByID(@PathVariable Long id) {
        User user = userManagementService.getUserByID(id);
        if (user == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "User with identifier " + id + " not found.", null);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "User with identifier " + id + " was successfully found.", user);
    }

    /**
     * Retrieves all users.
     *
     * @return A ResponseDTO containing a list of all users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public ResponseDTO<List<User>> getAllUsers() {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Fetched all users from database.", userManagementService.getAllUsers());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return A ResponseDTO containing the retrieved user or an error message if the user is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseDTO<User> getUserByEmail(@PathVariable String email) {
        User user = userManagementService.getUserByEmail(email);
        if (user == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "User with email " + email + " not found.", user);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "User with email " + email + " was successfully found.", user);
    }

    /**
     * Retrieves users by their name.
     *
     * @param name The name of the users.
     * @return A ResponseDTO containing a list of retrieved users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/name/{name}")
    public ResponseDTO<List<User>> getUsersByName(@PathVariable String name) {
        return new ResponseDTO<>(HttpStatus.OK.value(),
                "Fetched users from database with the given name " + name + ".", userManagementService.getUsersByName(name));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param email The email of the user to be deleted.
     * @return A ResponseDTO containing the deleted user or an error message if the user is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseDTO<User> delete(@PathVariable String email) {
        User deletedUser = userManagementService.deleteUser(email);
        if (deletedUser == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(),
                    "User with an email " + email + " not found.", null);
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),
                "User with an email " + email + " successfully removed.", deletedUser);
    }

    /**
     * Changes the role of a user.
     *
     * @param changeUserRoleDTO DTO containing the user ID and the new role.
     * @return A ResponseDTO containing the updated user or an error message if the user is not found or the request is invalid.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/change/role")
    public ResponseDTO<User> changeUserRole(@RequestBody ChangeUserRoleDTO changeUserRoleDTO) {
        if (changeUserRoleDTO == null) {
            return new ResponseDTO<>(HttpStatus.FORBIDDEN.value(), "Empty request body.", null);
        }
        ResponseDTO<User> responseDTO = new ResponseDTO<>();
        try {
            User user = userManagementService.changeUserRole(changeUserRoleDTO);
            if (user == null) {
                responseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                responseDTO.setMessage(String.format(USER_NOT_FOUND, changeUserRoleDTO.userEmail()));
                responseDTO.setData(null);
            } else {
                responseDTO.setStatus(HttpStatus.OK.value());
                responseDTO.setMessage(String.format(USER_ROLE_UPDATED, changeUserRoleDTO.userEmail(), changeUserRoleDTO.userRole()));
                responseDTO.setData(user);
            }
        } catch (RuntimeException e) {
            responseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            responseDTO.setMessage(e.getMessage());
            responseDTO.setData(null);
        }

        return responseDTO;
    }
}
