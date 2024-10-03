/**
 * Copyright 2024
 * Name: InitUserDTO
 */
package com.anhvt.messengerbackend.dto.user;

import com.anhvt.messengerbackend.dto.AuthUserDTO;
import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class InitUserDTO {

    private AuthUserDTO user;

    private List<GroupWrapperDTO> groupsWrapper;
}
