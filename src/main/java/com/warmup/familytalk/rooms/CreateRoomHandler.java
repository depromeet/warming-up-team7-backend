package com.warmup.familytalk.rooms;

import com.warmup.familytalk.global.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.warmup.familytalk.chats.ChatConfig.CHAT_URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRoomHandler implements Handler {

    private final RoomsService roomsService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        Mono<Room> room = request.bodyToMono(RoomCreateRequest.class)
                .flatMap(it -> roomsService.create(it.toEntity()));
        return writeResponse(room);
    }

    private static Mono<ServerResponse> writeResponse(Publisher<Room> room) {
        return Mono.from(room)
                .flatMap(it -> ServerResponse
                        .created(URI.create(CHAT_URI + it.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }
}