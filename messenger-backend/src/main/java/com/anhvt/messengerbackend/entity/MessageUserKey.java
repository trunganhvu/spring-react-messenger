/**
 * Copyright 2024
 * Name: MessageUserKey
 */
package com.anhvt.messengerbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageUserKey {

    @Column(name = "message_id")
    private int messageId;

    @Column(name = "user_id")
    private int userId;

    @Override
    public int hashCode() {
        return Objects.hash(messageId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MessageUserKey groupRoleKey = (MessageUserKey) obj;
        return messageId == groupRoleKey.messageId &&
                userId == groupRoleKey.userId;
    }
}