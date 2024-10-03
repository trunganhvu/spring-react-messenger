/**
 * Copyright 2024
 * Name: ApiController
 */
package com.anhvt.messengerbackend.controller;

import com.anhvt.messengerbackend.dto.AuthenticationUserDTO;
import com.anhvt.messengerbackend.dto.GroupMemberDTO;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.GroupRoleKey;
import com.anhvt.messengerbackend.entity.GroupUser;
import com.anhvt.messengerbackend.mapper.GroupMapper;
import com.anhvt.messengerbackend.service.GroupService;
import com.anhvt.messengerbackend.service.GroupUserJoinService;
import com.anhvt.messengerbackend.service.UserService;
import com.anhvt.messengerbackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * User create a new account
     *
     * @param userDTO AuthenticationUserDTO
     * @return ResponseEntity {@link ResponseEntity}
     */
    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody AuthenticationUserDTO userDTO) {
        try {
            userService.createUser(userDTO);
        } catch (Exception e) {
            log.error("Error while registering user : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("User saved successfully");
    }

    /**
     * Fetch all users not in a conversation
     *
     * @param groupUrl String
     * @return List of {@link GroupMemberDTO}
     */
    @GetMapping(value = "/all/{groupUrl}")
    public List<GroupMemberDTO> fetchAllUsersNotInGroup(@PathVariable String groupUrl) {
        // Find group
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        GroupRoleKey groupRoleKey = new GroupRoleKey();
        groupRoleKey.setGroupId(groupId);

        // Get users in group
        List<GroupUser> groupUsers = groupUserJoinService.findAllByGroupId(groupId);
        Object[] objects = groupUsers.stream().map(GroupUser::getUserId).toArray();
        int[] ids = new int[objects.length];

        // Set into int array
        for (int i = 0; i < objects.length; i++) {
            ids[i] = (int) objects[i];
        }
        return userService.fetchAllUsersNotInGroup(ids);
    }

    /**
     * Fetch all users in a conversation
     *
     * @param groupUrl string
     * @return List of {@link GroupMemberDTO}
     */
    @GetMapping(value = "/group/{groupUrl}")
    public List<?> fetchAllUsers(@PathVariable String groupUrl) throws Exception {
        List<GroupMemberDTO> groupMemberDTOS = new ArrayList<>();

        // Get group
        GroupEntity groupByUrl = groupService.getGroupByUrl(groupUrl);
        if (groupByUrl == null) {
            log.error("Group not found with URL {}", groupUrl);
            throw new Exception("Group cannot be found by URL");
        }

        groupByUrl
                .getGroupUsers()
                .forEach(groupUser -> groupMemberDTOS.add(groupMapper.toGroupMemberDTO(groupUser)));
        groupMemberDTOS.sort(Comparator.comparing(GroupMemberDTO::isAdmin).reversed());
        return groupMemberDTOS;
    }

    /**
     * Add user to a group conversation
     *
     * @param userId   int value for user ID
     * @param groupUrl String value for the group url
     * @return {@link ResponseEntity}, 200 if everything is ok or 500 if an error occurred
     */
    @GetMapping(value = "/add/{userId}/{groupUrl}")
    public ResponseEntity<?> addUserToConversation(@PathVariable int userId,
                                                                @PathVariable String groupUrl) {
        try {
//            return ResponseEntity.ok().body(addedUsername + " has been added to " + groupService.getGroupName(groupUrl));
            return ResponseEntity.ok().body(groupService.addUserToConversation(userId, groupUrl));
        } catch (Exception e) {
            log.error("Error when trying to add user to conversation : {}", e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     *
     * @param request HttpServletRequest
     * @param userId Integer
     * @param groupUrl String
     * @return ResponseEntity String
     */
    @GetMapping(value = "/remove/{userId}/group/{groupUrl}")
    public ResponseEntity<?> removeUserFromConversation(HttpServletRequest request,
                                                        @PathVariable Integer userId,
                                                        @PathVariable String groupUrl) {
        try {
            return ResponseEntity.ok()
                .body(groupUserJoinService.doUserAction(request, userId, groupUrl, "delete"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     *
     * @param request HttpServletRequest
     * @param userId Integer
     * @param groupUrl String
     * @return ResponseEntity String
     */
    @GetMapping(value = "/grant/{userId}/group/{groupUrl}")
    public ResponseEntity<?> grantUserAdminInConversation(HttpServletRequest request,
                                                          @PathVariable Integer userId,
                                                          @PathVariable String groupUrl) {
        try {
            return ResponseEntity.ok()
                .body(groupUserJoinService.doUserAction(request, userId, groupUrl, "grant"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     *
     * @param request HttpServletRequest
     * @param userId Integer
     * @param groupUrl String
     * @return ResponseEntity String
     */
    @GetMapping(value = "/remove/admin/{userId}/group/{groupUrl}")
    public ResponseEntity<?> removeAdminUserFromConversation(HttpServletRequest request,
                                                             @PathVariable Integer userId,
                                                             @PathVariable String groupUrl) {
        try {
            return ResponseEntity.ok()
                .body(groupUserJoinService.doUserAction(request, userId, groupUrl, "removeAdmin"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     *
     * @param request HttpServletRequest
     * @param userId Integer
     * @param groupUrl String
     * @return ResponseEntity String
     */
    @GetMapping(value = "/leave/{userId}/group/{groupUrl}")
    public ResponseEntity<?> leaveConversation(HttpServletRequest request,
                                               @PathVariable Integer userId,
                                               @PathVariable String groupUrl) {
        try {
            return ResponseEntity.ok()
                .body(groupUserJoinService.doUserAction(request, userId, groupUrl, "removeUser"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

//    private ResponseEntity<?> doUserAction(HttpServletRequest request, Integer userId,
//                                           String groupUrl, String action) {
//        Cookie cookie = WebUtils.getCookie(request, StaticVariable.SECURE_COOKIE);
//        if (cookie == null) {
//            return ResponseEntity.status(401).build();
//        }
//        String cookieToken = cookie.getValue();
//        // Get username from jwt in cookie
//        String username = jwtUtil.getUserNameFromJwtToken(cookieToken);
//        // Find user entity
//        UserEntity userEntity = userService.findByNameOrEmail(username, username);
//
//        if (userEntity != null) {
//            int groupId = groupService.findGroupIdByUrl(groupUrl);
//            String userToChange = userService.findUsernameById(userId);
//
//            int adminUserId = userEntity.getId();
//            if (action.equals("removeUser")) {
//                groupUserJoinService.removeUserFromConversation(userId, groupId);
//            }
//            if (userService.checkIfUserIsAdmin(adminUserId, groupId)) {
//                try {
//                    switch (action) {
//                        case "grant" -> {
//                            groupUserJoinService.grantUserAdminInConversation(userId, groupId);
//                            return ResponseEntity.ok().body(userToChange + " has been granted administrator to " + groupService.getGroupName(groupUrl));
//                        }
//                        case "delete" -> {
//                            groupUserJoinService.removeUserFromConversation(userId, groupId);
//                            return ResponseEntity.ok().body(userToChange + " has been removed from " + groupService.getGroupName(groupUrl));
//                        }
//                        case "removeAdmin" -> {
//                            groupUserJoinService.removeUserAdminFromConversation(userId, groupId);
//                            return ResponseEntity.ok().body(userToChange + " has been removed from administrators of " + groupService.getGroupName(groupUrl));
//                        }
//                    }
//                } catch (Exception e) {
//                    log.warn("Error during performing {} : {}", action, e.getMessage());
//                    return ResponseEntity.status(500).build();
//                }
//            }
//        }
//        return ResponseEntity.status(401).build();
//    }
}
