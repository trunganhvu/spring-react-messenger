/**
 * Copyright 2024
 * Name: AuthenticationController
 */
package com.anhvt.messengerbackend.controller;

import com.anhvt.messengerbackend.dto.AuthUserDTO;
import com.anhvt.messengerbackend.dto.JwtDTO;
import com.anhvt.messengerbackend.dto.user.GroupDTO;
import com.anhvt.messengerbackend.dto.user.InitUserDTO;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.mapper.GroupMapper;
import com.anhvt.messengerbackend.mapper.UserMapper;
import com.anhvt.messengerbackend.service.CustomUserDetailsService;
import com.anhvt.messengerbackend.service.GroupService;
import com.anhvt.messengerbackend.service.UserService;
import com.anhvt.messengerbackend.util.JwtUtil;
import com.anhvt.messengerbackend.util.StaticVariable;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST})
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Value("${cookie.max-age}")
    private int COOKIE_MAX_AGE;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/auth")
    public AuthUserDTO createAuthenticationToken(@RequestBody JwtDTO authenticationRequest,
                                                 HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            // Get user from DB(user entity) and cast to UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            // Gen token base on UserDetails
            String token = jwtTokenUtil.generateToken(userDetails);
            Cookie jwtAuthToken = new Cookie(StaticVariable.SECURE_COOKIE, token);  // Set token in cookie and send to client
            jwtAuthToken.setHttpOnly(true);
            jwtAuthToken.setSecure(false);
            jwtAuthToken.setPath("/");
//        cookie.setDomain("http://localhost");
            jwtAuthToken.setMaxAge(COOKIE_MAX_AGE); // 2 hours

            // Create cookie and set to response
            response.addCookie(jwtAuthToken);

            UserEntity user = userService.findByNameOrEmail(authenticationRequest.getUsername(),
                    authenticationRequest.getUsername());
            log.debug("User authenticated successfully");
            return userMapper.toLightUserDTO(user);
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> fetchInformation(HttpServletResponse response) {
        Cookie cookie = new Cookie(StaticVariable.SECURE_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/csrf")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }

    @GetMapping(value = "/fetch")
    public InitUserDTO fetchInformation(HttpServletRequest request) {
        return userMapper.toUserDTO(getUserEntity(request));
    }

    @PostMapping(value = "/create")
    public GroupDTO createGroupChat(HttpServletRequest request, @RequestBody String payload) {
        UserEntity user = getUserEntity(request);
        Gson gson = new Gson();
        GroupDTO groupDTO = gson.fromJson(payload, GroupDTO.class);
        GroupEntity groupEntity = groupService.createGroup(user.getId(), groupDTO.getName());
        return groupMapper.toGroupDTO(groupEntity, user.getId());
    }

    private UserEntity getUserEntity(HttpServletRequest request) {
        UserEntity user = new UserEntity();
        Cookie cookie = WebUtils.getCookie(request, StaticVariable.SECURE_COOKIE);
        if (cookie != null) {
            String jwtToken = cookie.getValue();
            String username = jwtTokenUtil.getUserNameFromJwtToken(jwtToken);
            user = userService.findByNameOrEmail(username, username);
        }
        return user;
    }
}