/**
 * Copyright 2024
 * Name: InputTransportDTO
 */
package com.anhvt.messengerbackend.dto;

import com.anhvt.messengerbackend.enums.MessageTypeEnum;
import com.anhvt.messengerbackend.enums.TransportActionEnum;
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
public class InputTransportDTO {

    private int userId;

    private TransportActionEnum action;

    private String wsToken;

    private String groupUrl;

    private String message;

    private MessageTypeEnum messageType;

    private int messageId;
}
