/**
 * Copyright 2024
 * Name: AuthUserDTO
 */
package com.anhvt.messengerbackend.dto;

import com.anhvt.messengerbackend.dto.user.GroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
@Builder
public class AuthUserDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String firstGroupUrl;

    private String wsToken;

    private String color;

    private List<GroupDTO> groups;
}

