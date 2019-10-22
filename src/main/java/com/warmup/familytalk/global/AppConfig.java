package com.warmup.familytalk.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class AppConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(HealthHandler healthHandler) {
        return route(GET("/l7_health_check"), healthHandler::handle);
//                .andRoute(POST("/rooms"), createRoomsHandler::handle)
    }
}
