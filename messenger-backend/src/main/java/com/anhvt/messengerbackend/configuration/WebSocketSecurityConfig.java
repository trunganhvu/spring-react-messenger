/**
 * Copyright 2024
 * Name: WebSocketSecurityConfig
 */
package com.anhvt.messengerbackend.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Configuration
@EnableWebSocketSecurity
@ConditionalOnProperty(name = "websocket.csrf.enable", havingValue = "1")
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages.anyMessage().permitAll();
        return messages.build();
    }

//    @Bean
//    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages.nullDestMatcher().authenticated().simpDestMatchers("/topic").hasAuthority(Role.ADMIN.getAuthority())
//                // matches any destination that starts with /topic/
//                // (i.e. cannot send messages directly to /topic/)
//                // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
//                // /topic/messages-user<id>)
//                .simpDestMatchers("/topic/**").authenticated()
//                // message types other than MESSAGE and SUBSCRIBE
//                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
//                // catch all
//                .anyMessage().denyAll();
//        return messages.build();
//    }
}
