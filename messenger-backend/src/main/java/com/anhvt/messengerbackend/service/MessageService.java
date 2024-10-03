/**
 * Copyright 2024
 * Name: MessageService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.dto.MessageDTO;
import com.anhvt.messengerbackend.dto.WrapperMessageDTO;
import com.anhvt.messengerbackend.dto.search.FullTextSearchDatabaseResponse;
import com.anhvt.messengerbackend.dto.search.FullTextSearchDatabaseResponseDTO;
import com.anhvt.messengerbackend.dto.search.FullTextSearchResponseDTO;
import com.anhvt.messengerbackend.entity.FileEntity;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.MessageEntity;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.enums.MessageTypeEnum;
import com.anhvt.messengerbackend.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class MessageService {

    private static Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    public MessageEntity createAndSaveMessage(int userId, int groupId, String type, String data) {
        MessageEntity msg = new MessageEntity(userId, groupId, type, data);
        return messageRepository.save(msg);
    }

    public MessageEntity save(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }

    public List<MessageEntity> findByGroupId(int id, int offset) {
        List<MessageEntity> list;
        if (offset == -1) {
            list = messageRepository.findLastMessagesByGroupId(id);
        } else {
            list = messageRepository.findByGroupIdAndOffset(id, offset);
        }
        return list;
    }

    public void deleteAllMessagesByGroupId(int groupId) {
        messageRepository.deleteMessagesDataByGroupId(groupId);
    }

    public MessageEntity findLastMessage(int groupId) {
        return messageRepository.findLastMessageByGroupId(groupId);
    }

    public int findLastMessageIdByGroupId(int groupId) {
        return messageRepository.findLastMessageIdByGroupId(groupId);
    }

    /**
     * Create a MessageDTO
     * Sent with user's initials
     *
     * @param id       of the message saved in DB
     * @param userId   int value for user ID
     * @param date     String of message sending date
     * @param group_id int value for group ID
     * @param message  string for the message content
     * @return a {@link MessageDTO}
     */
    public MessageDTO createMessageDTO(int id, String type, int userId, String date, int group_id, String message) {
        UserEntity user = userService.findById(userId);
        String fileUrl = "";
        String initials = user.getFirstName().substring(0, 1).toUpperCase() + user.getLastName().substring(0, 1).toUpperCase();
        String sender = StringUtils.capitalize(user.getFirstName()) +
                " " +
                StringUtils.capitalize(user.getLastName());
        if (type.equals(MessageTypeEnum.FILE.toString())) {
            FileEntity fileEntity = fileService.findByFkMessageId(id);
            fileUrl = fileEntity.getUrl();
        }
        return new MessageDTO(id, type, message, userId, group_id, null, sender, date, initials, user.getColor(), fileUrl, userId == id);
    }

    public static String createUserInitials(String firstAndLastName) {
        String[] names = firstAndLastName.split(",");
        return names[0].substring(0, 1).toUpperCase() + names[1].substring(0, 1).toUpperCase();
    }

    @Transactional
    public List<Integer> createNotificationList(int userId, String groupUrl) {
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        List<Integer> toSend = new ArrayList<>();
        Optional<GroupEntity> optionalGroupEntity = groupService.findById(groupId);
        if (optionalGroupEntity.isPresent()) {
            GroupEntity groupEntity = optionalGroupEntity.get();
            groupEntity.getUserEntities().forEach(userEntity -> toSend.add(userEntity.getId()));
        }
        return toSend;
    }

    public MessageDTO createNotificationMessageDTO(MessageEntity msg, int userId) {
        String groupUrl = groupService.getGroupUrlById(msg.getGroup_id());
        UserEntity user = userService.findById(userId);
        String firstName = userService.findFirstNameById(msg.getUser_id());
        String initials = userService.findUsernameById(msg.getUser_id());
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(msg.getId());
        if (msg.getType().equals(MessageTypeEnum.FILE.toString())) {
            String url = fileService.findFileUrlByMessageId(msg.getId());
            messageDTO.setFileUrl(url);
        }
        messageDTO.setType(msg.getType());
        messageDTO.setMessage(msg.getMessage());
        messageDTO.setUserId(msg.getUser_id());
        messageDTO.setGroupUrl(groupUrl);
        messageDTO.setGroupId(msg.getGroup_id());
        messageDTO.setSender(firstName);
        messageDTO.setTime(msg.getCreatedAt().toString());
        messageDTO.setInitials(createUserInitials(initials));
        messageDTO.setColor(user.getColor());
        messageDTO.setMessageSeen(msg.getUser_id() == userId);
        return messageDTO;
    }

    // TODO check that the request is authorized by user making the call
    public List<String> getMultimediaContentByGroup(String groupUrl) {
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        return fileService.getFilesUrlByGroupId(groupId);
    }

    public FullTextSearchResponseDTO searchMessages(int userId, String searchText) {
        // List group
        List<Integer> groupIds = groupUserJoinService.findAllGroupsByUserId(userId);
        List<FullTextSearchDatabaseResponse> responseFromDB =
                messageRepository.findMessagesBySearchQuery(searchText, groupIds);
        FullTextSearchResponseDTO result = new FullTextSearchResponseDTO();
        Map<Integer, List<FullTextSearchDatabaseResponse>> studlistGrouped =
                responseFromDB.stream().collect(Collectors.groupingBy(FullTextSearchDatabaseResponse::getId));
        result.setMatchingText(searchText);
        List<FullTextSearchDatabaseResponseDTO> matchingMessages = new ArrayList<>();
        for (Map.Entry<Integer, List<FullTextSearchDatabaseResponse>> entry : studlistGrouped.entrySet()) {
            if (entry.getValue().get(0) != null) {
                FullTextSearchDatabaseResponseDTO fullTextSearchDTO = new FullTextSearchDatabaseResponseDTO();
                List<String> messages = entry.getValue().stream().map(FullTextSearchDatabaseResponse::getMessage).toList();
                fullTextSearchDTO.setMessages(messages);
                fullTextSearchDTO.setId(entry.getKey());
                fullTextSearchDTO.setGroupUrl(entry.getValue().get(0).getGroupUrl());
                fullTextSearchDTO.setGroupName(entry.getValue().get(0).getGroupName());
                matchingMessages.add(fullTextSearchDTO);
            }
        }
        result.setMatchingMessages(matchingMessages);
        return result;
    }

    public WrapperMessageDTO getConversationMessage(String url, int messageId) throws Exception {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        GroupEntity group = groupService.getGroupByUrl(url);
        if (group == null) {
            log.error("Group not found with URL {}", url);
            throw new Exception("Group cannot be found by URL");
        }
        // Get all message
        List<MessageEntity> newMessages = this.findByGroupId(group.getId(), messageId);

        // Id of last message
        int lastMessageId = newMessages != null && !newMessages.isEmpty()
                ? newMessages.get(0).getId()
                : 0;
        List<MessageEntity> afterMessages = this.findByGroupId(group.getId(), lastMessageId);
        if (newMessages != null) {
            newMessages.forEach(msg ->
                    messageDTOS.add(this
                            .createMessageDTO(msg.getId(), msg.getType(), msg.getUser_id(),
                                    msg.getCreatedAt().toString(), msg.getGroup_id(), msg.getMessage()))
            );
        }

        return WrapperMessageDTO.builder()
                .isActiveCall(group.isActiveCall())
                .callUrl(group.getCallUrl())
                .isLastMessage(afterMessages != null && afterMessages.isEmpty())
                .messages(messageDTOS)
                .groupName(group.getName())
                .build();

    }
}

