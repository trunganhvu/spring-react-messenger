/**
 * Copyright 2024
 * Name: WebSocketConfig
 */
package com.anhvt.messengerbackend.configuration;

import com.anhvt.messengerbackend.controller.TestWebsocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 10/8/2024
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(myHandler(), "/myhandler");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new TestWebsocketHandler();
    }

}
