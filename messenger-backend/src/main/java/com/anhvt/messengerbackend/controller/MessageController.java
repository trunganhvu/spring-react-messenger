/**
 * Copyright 2024
 * Name: MessageController
 */
package com.anhvt.messengerbackend.controller;

import com.anhvt.messengerbackend.dto.WrapperMessageDTO;
import com.anhvt.messengerbackend.service.GroupUserJoinService;
import com.anhvt.messengerbackend.service.MessageService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@RequestMapping(value = "/messages")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class MessageController {

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    /**
     *
     * @param groupUrl String
     * @param offset int
     * @return WrapperMessageDTO
     * @throws Exception Exception
     */
    @GetMapping(value = "{offset}/group/{groupUrl}")
    public WrapperMessageDTO fetchGroupMessages(@PathVariable @NonNull String groupUrl,
                                                @PathVariable int offset) throws Exception {
        log.debug("Fetching messages from conversation");
        return messageService.getConversationMessage(groupUrl, offset);
    }

    /**
     *
     * @param groupUrl String
     * @param userId int
     */
    @GetMapping(value = "seen/group/{groupUrl}/user/{userId}")
    public void markMessageAsSeen(@PathVariable String groupUrl, @PathVariable int userId) {
        log.debug("Mark message as seen");
        groupUserJoinService.saveLastMessageDate(userId, groupUrl);
    }
}
