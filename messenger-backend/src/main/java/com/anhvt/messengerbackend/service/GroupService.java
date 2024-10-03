/**
 * Copyright 2024
 * Name: GroupService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.dto.GroupMemberDTO;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.GroupRoleKey;
import com.anhvt.messengerbackend.entity.GroupUser;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.enums.GroupTypeEnum;
import com.anhvt.messengerbackend.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    public int findGroupIdByUrl(String url) {
        return groupRepository.findGroupIdByUrl(url);
    }

    public GroupEntity getGroupByUrl(String url) {
        return groupRepository.getGroupByUrl(url);
    }

    public List<Integer> getAllUsersIdByGroupUrl(String groupUrl) {
        int groupId = groupRepository.findGroupIdByUrl(groupUrl);
        List<GroupUser> users = groupUserJoinService.findAllByGroupId(groupId);
        return users.stream().map(GroupUser::getUserId).collect(Collectors.toList());
    }

    public void saveGroup(GroupEntity group) {
        try {
            groupRepository.save(group);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String getGroupName(String url) {
        return groupRepository.getGroupEntitiesBy(url);
    }

    public String getGroupUrlById(int id) {
        return groupRepository.getGroupUrlById(id);
    }

    @Transactional
    public GroupMemberDTO addUserToConversation(int userId, String groupUrl) {
        GroupEntity groupEntity = groupRepository.getGroupByUrl(groupUrl);
        // Check group exist
        if (groupEntity == null) {
            log.error("Group not found");
            throw new RuntimeException("Group not found");
        }

        // Check group for users
        if (groupEntity.getGroupTypeEnum().equals(GroupTypeEnum.SINGLE)) {
            log.error("This group can not insert any users");
            throw new RuntimeException("This group can not insert any users");
        }
        Date date = new Date();
        Timestamp currentTimeStamp = new Timestamp(date.getTime());

        UserEntity user = userService.findById(userId);
        GroupUser groupUser = GroupUser.builder()
                .groupUsers(groupEntity)
                .userId(userId)
                .userEntities(user)
                .groupId(groupEntity.getId())
                .lastMessageSeenDate(currentTimeStamp)
                .role(0)
                .build();

        GroupUser groupUser1 = groupUserJoinService.save(groupUser);
        groupEntity.getGroupUsers().add(groupUser1);
        groupRepository.save(groupEntity);
        return new GroupMemberDTO(user.getId(), user.getFirstName(), user.getLastName(), false);
    }

    public GroupEntity createGroup(int userId, String name) {
        GroupUser groupUser = new GroupUser();
        GroupEntity group = new GroupEntity(name);
        group.setName(name);
        group.setUrl(UUID.randomUUID().toString());
        group.setGroupTypeEnum(GroupTypeEnum.GROUP);
        GroupEntity savedGroup = groupRepository.save(group);
        UserEntity user = userService.findById(userId);
        GroupRoleKey groupRoleKey = new GroupRoleKey();
        groupRoleKey.setUserId(userId);
        groupRoleKey.setGroupId(savedGroup.getId());
        groupUser.setGroupId(savedGroup.getId());
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        groupUser.setLastMessageSeenDate(ts);
        groupUser.setUserId(userId);
        groupUser.setRole(1);
        groupUser.setUserEntities(user);
        groupUser.setGroupUsers(group);
        groupUserJoinService.save(groupUser);
        return savedGroup;
    }

    public Optional<GroupEntity> findById(int groupId) {
        return groupRepository.findById(groupId);
    }

    public void createConversation(int id1, int id2) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(null);
        groupEntity.setUrl(UUID.randomUUID().toString());
        groupEntity.setGroupTypeEnum(GroupTypeEnum.SINGLE);
        GroupEntity savedGroup = groupRepository.save(groupEntity);

        UserEntity user1 = userService.findById(id1);
        UserEntity user2 = userService.findById(id2);

        GroupUser groupUser1 = new GroupUser();
        groupUser1.setGroupId(savedGroup.getId());
        groupUser1.setUserId(id1);

        groupUser1.setRole(0);
        groupUser1.setUserEntities(user1);
        groupUser1.setGroupUsers(groupEntity);

        GroupUser groupUser2 = new GroupUser();
        groupUser2.setUserId(savedGroup.getId());
        groupUser2.setGroupId(id2);
        groupUser2.setRole(0);
        groupUser2.setUserEntities(user2);
        groupUser2.setGroupUsers(groupEntity);
        groupUserJoinService.saveAll(Arrays.asList(groupUser1, groupUser2));
    }

    public void deleteGroup() {
        // TODO delete messages
        // TODO delete group
        // TODO delete multimedia
        // TODO user join service
    }
}