/**
 * Copyright 2024
 * Name: HandShakeInterceptor
 */
package com.anhvt.messengerbackend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Component
public class HandShakeInterceptor implements ApplicationListener<SessionDisconnectEvent> {

    private final Logger log = LoggerFactory.getLogger(HandShakeInterceptor.class);

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        log.warn("Test {}", event.getCloseStatus());
    }
}
