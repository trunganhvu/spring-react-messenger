/**
 * Copyright 2024
 * Name: GroupMemberDTO
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
public class GroupMemberDTO {

    private int userId;

    private String firstName;

    private String lastName;

    private boolean isAdmin;
}
