package com.warmup.familytalk.chats;

import com.warmup.familytalk.global.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoomHistoryHandler implements Handler {

    private final ChatManager chatManager;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        Flux<ChatMessage> messageFlux = chatManager.findHistoryByRoomId(id);
        return null;
    }
}
