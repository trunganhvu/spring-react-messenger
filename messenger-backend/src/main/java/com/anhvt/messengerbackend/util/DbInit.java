/**
 * Copyright 2024
 * Name: DbInit
 */
package com.anhvt.messengerbackend.util;

import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Service
public class DbInit implements CommandLineRunner {

    static Logger log = LoggerFactory.getLogger(DbInit.class);

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public DbInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            if (userService.findAll().isEmpty()) {
                List<String> sourceList = Arrays.asList("Thibaut", "Mark", "John", "Luke", "Steve");
                sourceList.forEach(val -> {
                    UserEntity user = new UserEntity();
                    user.setFirstName(val);
                    user.setLastName("Williams");
                    user.setPassword(passwordEncoder.encode("root"));
                    user.setMail(val.toLowerCase() + "@fastlitemessage.com");
                    user.setEnabled(true);
                    user.setColor(new ColorsUtils().getRandomColor());
                    user.setCredentialsNonExpired(true);
                    user.setAccountNonLocked(true);
                    user.setAccountNonExpired(true);
                    user.setWsToken(UUID.randomUUID().toString());
                    user.setRole(StaticVariable.USER_ROLE);
                    userService.save(user);
                });
                log.info("No entries detected in User table, data created");
            } else {
                log.info("Data already set in User table, skipping init step");
            }
        } catch (Exception e) {
            log.error("Cannot init DB : {}", e.getMessage());
        }
    }
}