/**
 * Copyright 2024
 * Name: GroupUserJoinService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.entity.GroupRoleKey;
import com.anhvt.messengerbackend.entity.GroupUser;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.repository.GroupUserJoinRepository;
import com.anhvt.messengerbackend.util.JwtUtil;
import com.anhvt.messengerbackend.util.StaticVariable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class GroupUserJoinService {

    private static final Logger log = LoggerFactory.getLogger(GroupUserJoinService.class);

    @Autowired
    private GroupUserJoinRepository groupUserJoinRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    public GroupUser save(GroupUser groupUser) {
        return groupUserJoinRepository.save(groupUser);
    }

    public void saveAll(List<GroupUser> groups) {
        try {
            groupUserJoinRepository.saveAll(groups);
        } catch (Exception e) {
            log.error("Cannot save user for conversation : {}", e.getMessage());
        }
    }

    public Optional<GroupUser> findById(GroupRoleKey id) {
        return groupUserJoinRepository.findById(id);
    }

    public List<GroupUser> findAll() {
        return groupUserJoinRepository.findAll();
    }

    public List<GroupUser> findAllByGroupId(int groupId) {
        return groupUserJoinRepository.getAllByGroupId(groupId);
    }

    public List<Integer> findAllGroupsByUserId(int userId) {
        return groupUserJoinRepository.getGroupUserByUserId(userId);
    }

    public GroupUser findGroupUser(int userId, int groupId) {
        return groupUserJoinRepository.getGroupUser(userId, groupId);
    }

    public void saveLastMessageDate(int userId, String groupUrl) {
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        GroupUser groupUser = groupUserJoinRepository.getGroupUser(userId, groupId);
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        groupUser.setLastMessageSeenDate(ts);

        save(groupUser);
    }

    public boolean checkIfUserIsAuthorizedInGroup(int userId, int groupId) {
        List<Integer> ids = groupUserJoinRepository.getUsersIdInGroup(groupId);
        return ids.stream().noneMatch(id -> id == userId);
    }


    public GroupUser grantUserAdminInConversation(int userId, int groupId) {
        return executeActionOnGroupUser(userId, groupId, 1);
    }

    public void removeUserAdminFromConversation(int userIdToDelete, int groupId) {
        executeActionOnGroupUser(userIdToDelete, groupId, 0);
    }

    private GroupUser executeActionOnGroupUser(int userId, int groupId, int role) {
        GroupRoleKey groupRoleKey = new GroupRoleKey(groupId, userId);
        Optional<GroupUser> optionalGroupUserToDelete = groupUserJoinRepository.findById(groupRoleKey);
        if (optionalGroupUserToDelete.isPresent()) {
            GroupUser groupUser = optionalGroupUserToDelete.get();
            groupUser.setRole(role);
            return groupUserJoinRepository.save(groupUser);
        }
        return null;
    }

    public void removeUserFromConversation(int userIdToDelete, int groupId) {
        GroupRoleKey groupRoleKey = new GroupRoleKey(groupId, userIdToDelete);
        try {
            Optional<GroupUser> optionalGroupUserToDelete = groupUserJoinRepository.findById(groupRoleKey);
            optionalGroupUserToDelete.ifPresent(groupUser -> groupUserJoinRepository.delete(groupUser));
            List<Integer> usersId = groupUserJoinRepository.getUsersIdInGroup(groupId);
            if (usersId.isEmpty()) {
                log.info("All users have left the group [groupId => {}]. Deleting messages...", groupId);
                messageService.deleteAllMessagesByGroupId(groupId);
                log.info("All messages have been successfully deleted");
            }
        } catch (Exception exception) {
            log.error("Error occurred during user removal : {}", exception.getMessage());
        }
    }

    public String doUserAction(HttpServletRequest request, Integer userId,
                                           String groupUrl, String action) throws RuntimeException {
        String result = "";
        Cookie cookie = WebUtils.getCookie(request, StaticVariable.SECURE_COOKIE);
        if (cookie == null) {
            throw new RuntimeException("Access denied: cookie is null");
        }
        String cookieToken = cookie.getValue();
        // Get username from jwt in cookie
        String username = jwtUtil.getUserNameFromJwtToken(cookieToken);
        // Find user entity
        UserEntity userEntity = userService.findByNameOrEmail(username, username);

        if (userEntity == null) {
            log.error("Current user not found");
            throw new RuntimeException("Current user not found");
        }
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        String userToChange = userService.findUsernameById(userId);

        if (userService.checkIfUserIsAdmin(userEntity.getId(), groupId)) {
            try {
                switch (action) {
                    case "grant" -> {
                        this.grantUserAdminInConversation(userId, groupId);
                        result = userToChange
                                + " has been granted administrator to "
                                + groupService.getGroupName(groupUrl);

                    }
                    case "delete" -> {
                        this.removeUserFromConversation(userId, groupId);
                        result = userToChange
                                + " has been removed from "
                                + groupService.getGroupName(groupUrl);
                    }
                    case "removeAdmin" -> {
                        this.removeUserAdminFromConversation(userId, groupId);
                        result = userToChange
                                + " has been removed from administrators of "
                                + groupService.getGroupName(groupUrl);
                    }
                    case "removeUser" -> {
                        this.removeUserFromConversation(userId, groupId);
                        result = userToChange
                                + " has been removed from administrators of "
                                + groupService.getGroupName(groupUrl);
                    }
                    default -> {
                        log.error("Action {} is not define", action);
                        throw new RuntimeException("Action is not define");
                    }
                }
            } catch (Exception e) {
                log.error("Error during performing {} : {}", action, e.getMessage());
                throw new RuntimeException("Exception: " + e.getMessage());
            }
        }

        return result;
    }
}

