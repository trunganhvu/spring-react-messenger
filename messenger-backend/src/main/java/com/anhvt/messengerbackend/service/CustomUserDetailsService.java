/**
 * Copyright 2024
 * Name: CustomUserDetailsService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetail to get by Username
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find by username in user entity
        UserEntity user = userService.findByNameOrEmail(username, username);

        // User not exist or user have been disable
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Account is not enabled");
        }
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
