/**
 * Copyright 2024
 * Name: GroupDTO
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
public class GroupDTO {

    private int id;

    private String url;

    private String name;

    private String groupType;

    private String lastMessageSender;

    private String lastMessage;

    private String lastMessageDate;

    private boolean isLastMessageSeen;
}
