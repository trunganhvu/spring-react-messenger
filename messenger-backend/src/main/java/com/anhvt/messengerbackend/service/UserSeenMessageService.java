/**
 * Copyright 2024
 * Name: UserSeenMessageService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.MessageEntity;
import com.anhvt.messengerbackend.entity.MessageUserEntity;
import com.anhvt.messengerbackend.repository.UserSeenMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class UserSeenMessageService {

    @Autowired
    private UserSeenMessageRepository seenMessageRepository;

    @Autowired
    private GroupService groupService;

    @Transactional
    public void saveMessageNotSeen(MessageEntity msg, int groupId) {
        Optional<GroupEntity> group = groupService.findById(groupId);

        group.ifPresent(groupEntity ->
                groupEntity.getUserEntities().forEach((user) -> {
                    MessageUserEntity message = new MessageUserEntity();
                    message.setMessageId(msg.getId());
                    message.setUserId(user.getId());
                    seenMessageRepository.save(message);
                }));
    }

    public MessageUserEntity findByMessageId(int messageId, int userId) {
        return seenMessageRepository.findAllByMessageIdAndUserId(messageId, userId);
    }

    public void saveMessageUserEntity(MessageUserEntity toSave) {
        seenMessageRepository.save(toSave);
    }
}