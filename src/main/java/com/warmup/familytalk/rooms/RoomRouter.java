package com.warmup.familytalk.rooms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
public class RoomRouter {

    static final String ROOMS_URL = "/rooms";

    @Bean
    public RouterFunction<ServerResponse> rooms(RoomHandler handle) {
        return route(POST(ROOMS_URL), handle::create)
                .andRoute(GET(ROOMS_URL.concat("/my")), handle::searchByUser)
                .andRoute(GET(ROOMS_URL.concat("/{id}")), handle::search)
                .andRoute(DELETE(ROOMS_URL.concat("/{id}")), handle::delete);
    }
}
