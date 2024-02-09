package com.Library.services;


import com.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.Library.services.UserManagementService.USER_NOT_FOUND;

/**
 * Service class that implements the UserDetailsService interface to load user details for authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = Logger.getLogger(UserDetailsServiceImpl.class.getCanonicalName());
    private final UserManagementService userManagementService;

    @Autowired
    public UserDetailsServiceImpl(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Loads user details by the given username (email).
     *
     * @param userEmail the email address of the user
     * @return UserDetails object containing user details if found, otherwise null.
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userManagementService.getUserByEmail(userEmail);

        if (user == null) {
            log.severe(String.format(USER_NOT_FOUND, userEmail));
            return null;
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getUserRole() == null ? "" : user.getUserRole().toString())
                .build();
    }
}