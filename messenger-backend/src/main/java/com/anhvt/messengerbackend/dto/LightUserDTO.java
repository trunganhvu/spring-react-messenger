/**
 * Copyright 2024
 * Name: LightUserDTO
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
public class LightUserDTO {

    public int id;

    public String firstName;

    public String lastName;

    private int groupRole;

    private String wsToken;
}

