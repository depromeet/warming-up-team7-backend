package com.warmup.familytalk.chats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChatRouter {

    private static final String CHAT_INFO_URL = "/123/";

    @Bean
    public RouterFunction<ServerResponse> history(ChatHistoryHandler chatHistoryHandler) {
        return route(GET(CHAT_INFO_URL), chatHistoryHandler::handle);
    }
}
