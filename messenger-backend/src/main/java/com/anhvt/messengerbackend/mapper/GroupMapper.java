/**
 * Copyright 2024
 * Name: GroupMapper
 */
package com.anhvt.messengerbackend.mapper;

import com.anhvt.messengerbackend.dto.GroupMemberDTO;
import com.anhvt.messengerbackend.dto.user.GroupDTO;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.GroupUser;
import com.anhvt.messengerbackend.entity.MessageEntity;
import com.anhvt.messengerbackend.entity.MessageUserEntity;
import com.anhvt.messengerbackend.enums.MessageTypeEnum;
import com.anhvt.messengerbackend.service.GroupUserJoinService;
import com.anhvt.messengerbackend.service.MessageService;
import com.anhvt.messengerbackend.service.UserSeenMessageService;
import com.anhvt.messengerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Service
public class GroupMapper {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserSeenMessageService seenMessageService;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    @Autowired
    private UserService userService;

    public GroupDTO toGroupDTO(GroupEntity grp, int userId) {
        GroupDTO grpDTO = new GroupDTO();
        grpDTO.setId(grp.getId());
        grpDTO.setName(grp.getName());
        grpDTO.setUrl(grp.getUrl());
        grpDTO.setGroupType(grp.getGroupTypeEnum().toString());
        GroupUser user = groupUserJoinService.findGroupUser(userId, grp.getId());
        MessageEntity msg = messageService.findLastMessage(grp.getId());
        if (msg != null) {
            String sender = userService.findFirstNameById(msg.getUser_id());
            MessageUserEntity messageUserEntity = seenMessageService.findByMessageId(msg.getId(), userId);
            grpDTO.setLastMessageSender(sender);
            if (messageUserEntity != null) {
                if (msg.getType().equals(MessageTypeEnum.FILE.toString())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String senderName = userId == msg.getUser_id() ? "You" : sender;
                    stringBuilder.append(senderName);
                    stringBuilder.append(" ");
                    stringBuilder.append("has send a file");
                    grpDTO.setLastMessage(stringBuilder.toString());
                } else {
                    grpDTO.setLastMessage(msg.getMessage());
                }
                grpDTO.setLastMessage(msg.getMessage());
                grpDTO.setLastMessageSeen(msg.getCreatedAt().after(user.getLastMessageSeenDate()));
                grpDTO.setLastMessageDate(msg.getCreatedAt().toString());
            }
        } else {
            grpDTO.setLastMessageDate(grp.getCreatedAt().toString());
            grpDTO.setLastMessageSeen(true);
        }
        return grpDTO;
    }

    public GroupMemberDTO toGroupMemberDTO(GroupUser groupUser) {
        return new GroupMemberDTO(groupUser.getUserEntities().getId(), groupUser.getUserEntities().getFirstName(), groupUser.getUserEntities().getLastName(), groupUser.getRole() == 1);
    }
}
