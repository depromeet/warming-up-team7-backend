package com.warmup.familytalk.chats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChatRouter {

    public static final String CHAT_INFO_URL = "/chatrooms";

    @Bean
    public RouterFunction<ServerResponse> history(ChatRoomHandler chatRoomHandler) {
        return route(GET(CHAT_INFO_URL + "/participants"), chatRoomHandler::allUserInCurrentRoom)
                .andRoute(GET(CHAT_INFO_URL), chatRoomHandler::searchByUser);
    }
}
