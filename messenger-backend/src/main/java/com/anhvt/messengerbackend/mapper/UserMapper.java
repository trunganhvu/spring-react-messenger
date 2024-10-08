/**
 * Copyright 2024
 * Name: UserMapper
 */
package com.anhvt.messengerbackend.mapper;

import com.anhvt.messengerbackend.dto.AuthUserDTO;
import com.anhvt.messengerbackend.dto.user.GroupDTO;
import com.anhvt.messengerbackend.dto.user.GroupWrapperDTO;
import com.anhvt.messengerbackend.dto.user.InitUserDTO;
import com.anhvt.messengerbackend.entity.GroupEntity;
import com.anhvt.messengerbackend.entity.UserEntity;
import com.anhvt.messengerbackend.util.ComparatorListGroupDTO;
import com.anhvt.messengerbackend.util.ComparatorListWrapperGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Service
public class UserMapper {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupCallMapper groupCallMapper;

    /**
     * Map a UserEntity to a UserDTO
     * The password is not sent
     *
     * @param userEntity the {@link UserEntity} to map
     * @return a {@link AuthUserDTO}
     */
    public InitUserDTO toUserDTO(UserEntity userEntity) {
        AuthUserDTO userDTO = new AuthUserDTO();
        InitUserDTO initUserDTO = new InitUserDTO();
        List<GroupWrapperDTO> groupWrapperDTOS = new ArrayList<>();

        userDTO.setId(userEntity.getId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setWsToken(userEntity.getWsToken());
        userDTO.setColor(userEntity.getColor());

        userEntity.getGroupSet().forEach(groupEntity -> {
                    GroupWrapperDTO groupWrapperDTO = new GroupWrapperDTO();
                    groupWrapperDTO.setGroup(groupMapper.toGroupDTO(groupEntity, userEntity.getId()));
                    groupWrapperDTO.setGroupCall(groupCallMapper.toGroupCall(groupEntity));
                    groupWrapperDTOS.add(groupWrapperDTO);
                }
        );
        groupWrapperDTOS.sort(new ComparatorListWrapperGroupDTO());

        Optional<GroupWrapperDTO> groupUrl = groupWrapperDTOS.stream().findFirst();
        String firstGroupUrl = groupUrl.isPresent() ? groupUrl.get().getGroup().getUrl() : "";

        userDTO.setFirstGroupUrl(firstGroupUrl);
        initUserDTO.setUser(userDTO);
        initUserDTO.setGroupsWrapper(groupWrapperDTOS);
        return initUserDTO;
    }

    public AuthUserDTO toLightUserDTO(UserEntity userEntity) {
        Set<GroupEntity> groups = userEntity.getGroupSet();
        List<GroupDTO> allUserGroups = new ArrayList<>(userEntity.getGroupSet().stream()
                .map((groupEntity) -> groupMapper.toGroupDTO(groupEntity, userEntity.getId())).toList());
        Optional<GroupEntity> groupUrl = groups.stream().findFirst();
        String lastGroupUrl = groupUrl.isPresent() ? groupUrl.get().getUrl() : "";
        allUserGroups.sort(new ComparatorListGroupDTO());
        return AuthUserDTO.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .firstGroupUrl(lastGroupUrl)
                .wsToken(userEntity.getWsToken())
                .groups(allUserGroups)
                .build();
//        return new AuthUserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), lastGroupUrl, userEntity.getWsToken(), userEntity.getColor(), allUserGroups);
    }
}
//    public AuthUserDTO toLightUserDTO(UserEntity userEntity) {
//        Set<GroupEntity> groups = userEntity.getGroupSet();
//        List<GroupDTO> allUserGroups = new ArrayList<>(userEntity.getGroupSet().stream()
//                .map((groupEntity) -> groupMapper.toGroupDTO(groupEntity, userEntity.getId())).toList());
//        Optional<GroupEntity> groupUrl = groups.stream().findFirst();
//        String lastGroupUrl = groupUrl.isPresent() ? groupUrl.get().getUrl() : "";
//        allUserGroups.sort(new ComparatorListGroupDTO());
//        return new AuthUserDTO().builder()
//                        .id(userEntity.getId())
//                .firstName(userEntity.getFirstName())
//                .lastName(userEntity.getLastName())
//                .firstGroupUrl(lastGroupUrl)
//                .wsToken(userEntity.getWsToken())
//                .groups(allUserGroups)
//                .build();
//    }
//}
