package com.warmup.familytalk.chats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ChatConfig {

    static final String CHAT_URI = "/chat/room/";

    @Bean
    public HandlerMapping webSocketMapping() {
        Map<String, WebSocketHandler> handlers = new HashMap<>();
        handlers.put(CHAT_URI.concat("**"), new ChatSocketHandler());

        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(handlers);
        simpleUrlHandlerMapping.setOrder(10);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
