package com.warmup.familytalk.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HealthRouter {

    static final String HEALTH_URL = "/l7_health_check";

    @Bean
    public RouterFunction<ServerResponse> health(HealthHandler healthHandler) {
        return route(GET(HEALTH_URL)
                        .and(accept(MediaType.APPLICATION_JSON)), healthHandler::handle);
    }
}
