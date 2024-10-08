/**
 * Copyright 2024
 * Name: WsConfig
 */
package com.anhvt.messengerbackend.configuration;

import com.anhvt.messengerbackend.controller.TestWebsocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer { //, WebSocketConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messenger").setAllowedOrigins("http://localhost:3000");
        registry.addEndpoint("/messenger", "/call")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//        webSocketHandlerRegistry.addHandler(myHandler(), "/myhandler");
//    }
//
//    @Bean
//    public WebSocketHandler myHandler() {
//        return new TestWebsocketHandler();
//    }
}

