package com.warmup.familytalk.global;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HealthHandler implements Handler{

    static final String SUCCESS_MESSAGE = "SUCCESS";

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse.ok()
                .body(Mono.just(SUCCESS_MESSAGE), String.class);
    }
}