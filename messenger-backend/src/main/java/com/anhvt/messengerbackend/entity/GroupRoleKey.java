/**
 * Copyright 2024
 * Name: GroupRoleKey
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
@AllArgsConstructor
@NoArgsConstructor
public class GroupRoleKey {

    @Column(name = "group_id")
    private int groupId;

    @Column(name = "user_id")
    private int userId;

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GroupRoleKey groupRoleKey = (GroupRoleKey) obj;
        return groupId == groupRoleKey.groupId &&
                userId == groupRoleKey.userId;
    }
}
