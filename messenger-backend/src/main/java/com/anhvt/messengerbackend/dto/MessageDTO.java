/**
 * Copyright 2024
 * Name: MessageDTO
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
public class MessageDTO {

    private int id;

    private String type;

    private String message;

    private int userId;

    private int groupId;

    private String groupUrl;

    private String sender;

    private String time;

    private String initials;

    private String color;

    private String fileUrl;

    private boolean isMessageSeen;
}
