package com.warmup.familytalk.rooms;

import com.warmup.familytalk.global.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.warmup.familytalk.chats.ChatConfig.CHAT_URI;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRoomHandler implements Handler {

    private final RoomService roomService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(RoomCreateRequest.class)
                .flatMap(it -> roomService.create(it.toEntity()))
                .flatMap(it -> created(URI.create(CHAT_URI + it.getId()))
                        .contentType(APPLICATION_JSON)
                        .build()
                );
    }
}