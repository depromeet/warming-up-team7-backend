package com.warmup.familytalk.chats;

import com.warmup.familytalk.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class ChatRoomHandler {

    private final ChatRoomManager chatRoomManager;
/*
    Mono<ServerResponse> search(ServerRequest request) {
        return chatRoomManager.findRoomByUserId(Long.parseLong(request.pathVariable("id")))
                              .flatMap(it -> ok().contentType(APPLICATION_JSON)
                                                 .body(fromObject(it)))
                              .switchIfEmpty(notFound().build());
    }*/

    Mono<ServerResponse> searchByUser(ServerRequest request) {
        return request.principal()
                      .map(Principal::getName)
                      .map(Long::valueOf)
                      .flatMap(chatRoomManager::findRoomByUserId)
                      .flatMap(room -> ServerResponse.ok()
                                                     .body(fromObject(room)));
    }

    Mono<ServerResponse> allUserInCurrentRoom(ServerRequest request) {
        Flux<User> participants = chatRoomManager.participants(1l);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(participants, User.class);
    }
}