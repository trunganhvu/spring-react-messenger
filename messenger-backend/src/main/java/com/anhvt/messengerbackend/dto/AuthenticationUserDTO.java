/**
 * Copyright 2024
 * Name: AuthenticationUserDTO
 */
package com.anhvt.messengerbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationUserDTO {

    private String firstname;

    private String lastname;

    private String password;

    private String email;
}