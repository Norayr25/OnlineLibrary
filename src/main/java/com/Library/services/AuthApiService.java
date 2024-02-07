//package com.example.Library.services;
//
//import repositores.com.Library.UserRepository;
//import entities.services.com.Library.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthApiService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    public AuthApiService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
//        User user = userRepository.getByEmail(userEmail);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + userEmail);
//        }
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .roles("USER")
//                .build();
//    }
//}