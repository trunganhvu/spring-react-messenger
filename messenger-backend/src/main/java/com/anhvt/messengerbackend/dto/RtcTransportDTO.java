/**
 * Copyright 2024
 * Name: RtcTransportDTO
 */
package com.anhvt.messengerbackend.dto;

import com.anhvt.messengerbackend.enums.RtcActionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RtcTransportDTO {

    private int userId;

    private String groupUrl;

    private RtcActionEnum action;

    private Object offer;

    private Object answer;

    private Object iceCandidate;
}
