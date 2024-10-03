/**
 * Copyright 2024
 * Name: WsFileController
 */
package com.anhvt.messengerbackend.controller;

import com.anhvt.messengerbackend.dto.MessageDTO;
import com.anhvt.messengerbackend.dto.OutputTransportDTO;
import com.anhvt.messengerbackend.entity.MessageEntity;
import com.anhvt.messengerbackend.enums.MessageTypeEnum;
import com.anhvt.messengerbackend.enums.TransportActionEnum;
import com.anhvt.messengerbackend.service.GroupService;
import com.anhvt.messengerbackend.service.MessageService;
import com.anhvt.messengerbackend.service.StorageService;
import com.anhvt.messengerbackend.service.UserSeenMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST})
public class WsFileController {

    private static final Logger log = LoggerFactory.getLogger(WsFileController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserSeenMessageService seenMessageService;

    /**
     * Receive file to put in DB and send it back to the group conversation
     *
     * @param file     The file to be uploaded
     * @param userId   int value for user ID sender of the message
     * @param groupUrl string value for the group URL
     * @return a {@link ResponseEntity} with HTTP code
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile file,
                                        @RequestParam(name = "userId") int userId,
                                        @RequestParam(name = "groupUrl") String groupUrl) {
        int groupId = groupService.findGroupIdByUrl(groupUrl);
        try {
            MessageEntity messageEntity = messageService.createAndSaveMessage(userId, groupId, MessageTypeEnum.FILE.toString(), "have send a file");
            storageService.store(file, messageEntity.getId());
            OutputTransportDTO res = new OutputTransportDTO();
            MessageDTO messageDTO = messageService.createNotificationMessageDTO(messageEntity, userId);
            res.setAction(TransportActionEnum.NOTIFICATION_MESSAGE);
            res.setObject(messageDTO);
            seenMessageService.saveMessageNotSeen(messageEntity, groupId);
            List<Integer> toSend = messageService.createNotificationList(userId, groupUrl);
            toSend.forEach(toUserId -> messagingTemplate.convertAndSend("/topic/user/" + toUserId, res));
        } catch (Exception e) {
            log.error("Cannot save file, caused by {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("files/groupUrl/{groupUrl}")
    public List<String> getMultimediaContent(@PathVariable String groupUrl) {
        return messageService.getMultimediaContentByGroup(groupUrl);
    }
}