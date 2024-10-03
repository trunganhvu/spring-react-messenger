/**
 * Copyright 2024
 * Name: JwtDTO
 */
package com.anhvt.messengerbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO implements Serializable {

    private String username;

    private String password;
}
