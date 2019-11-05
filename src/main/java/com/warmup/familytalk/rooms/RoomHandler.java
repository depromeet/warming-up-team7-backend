package com.warmup.familytalk.rooms;

import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

import static com.warmup.familytalk.chats.ChatConfig.CHAT_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class RoomHandler {

    private final RoomService roomService;

    private final UserService userService;

    Mono<ServerResponse> searchByUser(ServerRequest request) {
        return request.principal()
                      .map(Principal::getName)
                      .map(Long::valueOf)
                      .flatMap(roomService::findRoomByUserId)
                      .flatMap(room -> ServerResponse.ok()
                                                     .body(fromObject(room)));
    }

    Mono<ServerResponse> search(ServerRequest request) {
        return roomService.find(Long.parseLong(request.pathVariable("id")))
                          .flatMap(it -> ok().contentType(MediaType.APPLICATION_JSON)
                                             .body(fromObject(it)))
                          .switchIfEmpty(notFound().build());
    }

    Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(RoomCreateRequest.class)
                      .flatMap(it -> roomService.create(it, request.principal()
                                                                              .map(Principal::getName)
                                                                              .map(Long::valueOf)
                                                                              .flatMap(userService::findByUserId)))
                      .flatMap(it -> created(URI.create(CHAT_URL + it.getId()))
                              .contentType(APPLICATION_JSON)
                              .build());
    }

    Mono<ServerResponse> delete(ServerRequest request) {
        return roomService.remove(Long.parseLong(request.pathVariable("id")))
                          .flatMap(it -> ok().build())
                          .switchIfEmpty(notFound().build());
    }
}
