package com.Library.configs.providers;

import com.Library.services.common.UserRole;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomBasicAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public CustomBasicAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extract credentials from authentication object
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // Checking the super admin case. User signed with email/password equals to super_admin will have SUPER_ADMIN role,
        if (UserRole.SUPER_ADMIN.toString().equalsIgnoreCase(username) &&
                UserRole.SUPER_ADMIN.toString().equalsIgnoreCase(password)) {
            UserDetails userDetails = User
                    .withUsername(username)
                    .password(password)
                    .roles(String.valueOf(UserRole.SUPER_ADMIN))
                    .build();
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        } else {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null && userDetails.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            }
        }

        // If authentication fails, throw AuthenticationException
        throw new BadCredentialsException("Authentication failed: Invalid username or password.");
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationType);
    }
}