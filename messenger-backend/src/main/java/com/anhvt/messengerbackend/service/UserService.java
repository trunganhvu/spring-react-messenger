/**
 * Copyright 2024
 * Name: UserService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.dto.AuthenticationUserDTO;
import com.anhvt.messengerbackend.dto.GroupMemberDTO;
import com.anhvt.messengerbackend.entity.GroupRoleKey;
import com.anhvt.messengerbackend.entity.GroupUser;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.mapper.UserMapper;
import com.anhvt.messengerbackend.repository.UserRepository;
import com.anhvt.messengerbackend.util.ColorsUtils;
import com.anhvt.messengerbackend.util.StaticVariable;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Service
@Getter
@Setter
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupUserJoinService groupUserJoinService;

    private Map<Integer, String> wsSessions = new HashMap<>();

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void flush() {
        userRepository.flush();
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(AuthenticationUserDTO userDTO) throws Exception {
        try {
            if ((this.checkIfUserNameOrMailAlreadyUsed(userDTO.getEmail()))) {
                throw new Exception("Mail already used, please choose another");
            }
            UserEntity user = UserEntity.builder()
                    .firstName(userDTO.getFirstname())
                    .lastName(userDTO.getLastname())
                    .mail(userDTO.getEmail())
                    .password(passwordEncoder(userDTO.getPassword()))
                    .shortUrl(createShortUrl(userDTO.getFirstname(), userDTO.getLastname()))
                    .wsToken(UUID.randomUUID().toString()) // websocket token is uuid
                    .color(new ColorsUtils().getRandomColor())
                    .role(StaticVariable.USER_ROLE)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();

            userRepository.save(user);
            log.info("User saved successfully");
        } catch (Exception e) {
            log.error("Error while registering user : {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public List<GroupMemberDTO> fetchAllUsersNotInGroup(int[] ids) {
        List<GroupMemberDTO> groupMemberDTOS = new ArrayList<>();
        List<UserEntity> list = userRepository.getAllUsersNotAlreadyInConversation(ids);
        list.forEach(user -> groupMemberDTOS
                .add(new GroupMemberDTO(user.getId(), user.getFirstName(), user.getLastName(), false)));
        return groupMemberDTOS;
    }

    public String findUsernameWithWsToken(String token) {
        return userRepository.getUsernameWithWsToken(token);
    }

    public int findUserIdWithToken(String token) {
        return userRepository.getUserIdWithWsToken(token);
    }

    public UserEntity findByNameOrEmail(String str0, String str1) {
        return userRepository.getUserByFirstNameOrMail(str0, str1);
    }

    public boolean checkIfUserIsAdmin(int userId, int groupIdToCheck) {
        GroupRoleKey id = new GroupRoleKey(groupIdToCheck, userId);
        Optional<GroupUser> optional = groupUserJoinService.findById(id);
        if (optional.isPresent()) {
            GroupUser groupUser = optional.get();
            return groupUser.getRole() == 1;
        }
        return false;
    }

    public String createShortUrl(String firstName, String lastName) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        sb.append(".");
        sb.append(Normalizer.normalize(lastName.toLowerCase(), Normalizer.Form.NFD));
        boolean isShortUrlAvailable = true;
        int counter = 0;
        while (isShortUrlAvailable) {
            sb.append(counter);
            if (userRepository.countAllByShortUrl(sb.toString()) == 0) {
                isShortUrlAvailable = false;
            }
            counter++;
        }
        return sb.toString();
    }

    public String findUsernameById(int id) {
        return userRepository.getUsernameByUserId(id);
    }

    public String findFirstNameById(int id) {
        return userRepository.getFirstNameByUserId(id);
    }

    public UserEntity findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public String passwordEncoder(String str) {
        return passwordEncoder.encode(str);
    }

    public boolean checkIfUserNameOrMailAlreadyUsed(String mail) {
        return userRepository.countAllByMail(mail) > 0;
    }
}
