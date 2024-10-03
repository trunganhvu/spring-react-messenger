/**
 * Copyright 2024
 * Name: FullTextSearchDatabaseResponseDTO
 */
package com.anhvt.messengerbackend.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullTextSearchDatabaseResponseDTO {

    private Integer id;

    private String groupUrl;

    private List<String> messages;

    private String groupName;
}
