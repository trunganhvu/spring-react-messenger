/**
 * Copyright 2024
 * Name: WrapperMessageDTO
 */
package com.anhvt.messengerbackend.dto;

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
public class WrapperMessageDTO {

    private boolean isLastMessage;

    private String groupName;

    private boolean isActiveCall;

    private String callUrl;

    private List<MessageDTO> messages;
}

