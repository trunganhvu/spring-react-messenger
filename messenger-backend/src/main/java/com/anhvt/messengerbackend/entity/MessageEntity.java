/**
 * Copyright 2024
 * Name: MessageEntity
 */
package com.anhvt.messengerbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Entity
@Table(name = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    @Column(name = "msg_group_id")
    private int group_id;

    @Column(name = "msg_user_id")
    private int user_id;

    @Column(name = "type")
    private String type;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public MessageEntity(int userId, int groupId, String type, String message) {
        this.user_id = userId;
        this.group_id = groupId;
        this.type = type;
        this.message = message;
    }
}

