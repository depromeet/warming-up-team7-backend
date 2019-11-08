package com.warmup.familytalk.chats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class ChatRoomHandler {

    private final ChatRoomManager chatRoomManager;

    Mono<ServerResponse> search(ServerRequest request) {
        return chatRoomManager.findRoomByUserId(Long.parseLong(request.pathVariable("id")))
                              .flatMap(it -> ok().contentType(MediaType.APPLICATION_JSON)
                                                 .body(fromObject(it)))
                              .switchIfEmpty(notFound().build());
    }

    Mono<ServerResponse> searchByUser(ServerRequest request) {
        return getUserId(request)
                .flatMap(chatRoomManager::findRoomByUserId)
                .flatMap(room -> ServerResponse.ok()
                                               .body(fromObject(room)));
    }

    private Mono<Long> getUserId(ServerRequest request) {
        return request.principal()
                      .map(Principal::getName)
                      .map(Long::valueOf);
    }
/*
    Flux<ServerResponse> allUserInCurrentRoom(ServerRequest serverRequest){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(getUserId(serverRequest)
                              .flatMap(chatRoomManager::participants)

                        , Profile.class));
    }*/
}
