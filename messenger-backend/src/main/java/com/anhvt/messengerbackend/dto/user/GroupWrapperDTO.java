/**
 * Copyright 2024
 * Name: GroupWrapperDTO
 */
package com.anhvt.messengerbackend.dto.user;

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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupWrapperDTO {

    private GroupDTO group;

    private GroupCallDTO groupCall;
}
