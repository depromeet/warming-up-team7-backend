package com.warmup.familytalk.rooms;

import com.warmup.familytalk.global.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class RemoveRoomHandler implements Handler {

    private final RoomService roomService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        final long id = Long.parseLong(request.pathVariable("id"));
        return roomService.remove(id)
                          .flatMap(it -> ok().build())
                          .switchIfEmpty(notFound().build());
    }
}
