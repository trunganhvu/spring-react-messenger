/**
 * Copyright 2024
 * Name: SearchController
 */
package com.anhvt.messengerbackend.controller;

import com.anhvt.messengerbackend.dto.search.FullTextSearchDTO;
import com.anhvt.messengerbackend.dto.search.FullTextSearchResponseDTO;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.service.MessageService;
import com.anhvt.messengerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@RequestMapping(value = "/search")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    /**
     *
     * @param search FullTextSearchDTO
     * @param authentication Authentication
     * @return FullTextSearchResponseDTO
     */
    @PostMapping()
    public FullTextSearchResponseDTO searchInDiscussions(@RequestBody FullTextSearchDTO search,
                                                         Authentication authentication) {
        String name = authentication.getName();
        UserEntity user = this.userService.findByNameOrEmail(name, name);
        return messageService.searchMessages(user.getId(), search.getText());
    }
}
