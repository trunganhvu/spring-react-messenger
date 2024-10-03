/**
 * Copyright 2024
 * Name: FullTextSearchResponseDTO
 */
package com.anhvt.messengerbackend.dto.search;

import lombok.Getter;
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
public class FullTextSearchResponseDTO {

    private String matchingText;

    private List<FullTextSearchDatabaseResponseDTO> matchingMessages;
}
