package com.example.Library.controllers;

import com.example.Library.services.UserManagementService;
import com.example.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserManagementController {
    private final UserManagementService userManagementService;

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Long id) {
        return userManagementService.getUserByID(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userManagementService.getAllUsers();
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userManagementService.getUserByEmail(email);
    }

    @GetMapping("/name/{name}")
    public List<User> getUsersByName(@PathVariable String name) {
        return userManagementService.getUsersByName(name);
    }

    @PostMapping("/create")
    public String create(@RequestBody User user) {
        return userManagementService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userManagementService.deleteUser(id);
    }
}
