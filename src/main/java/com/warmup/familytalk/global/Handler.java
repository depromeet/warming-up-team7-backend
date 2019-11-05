package com.warmup.familytalk.global;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface Handler {

    Mono<ServerResponse> handle(ServerRequest request);
}
