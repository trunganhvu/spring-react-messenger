/**
 * Copyright 2024
 * Name: PingController
 */
package com.anhvt.messengerbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@RestController
@RequestMapping(value = "/")
public class PingController {

    private final Logger log = LoggerFactory.getLogger(PingController.class);

    /**
     *
     * @return String
     */
    @GetMapping("health-check")
    public String testRoute() {
        log.debug("Ping base route");
        return "Server status OK";
    }
}