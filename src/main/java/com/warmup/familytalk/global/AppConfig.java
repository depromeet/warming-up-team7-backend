package com.warmup.familytalk.global;

import com.warmup.familytalk.rooms.CreateRoomHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class AppConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(HealthHandler healthHandler,
                                                 CreateRoomHandler createRoomHandler) {
        return route(GET("/l7_health_check"), healthHandler::handle)
                .andRoute(POST("/rooms"), createRoomHandler::handle);
    }
}
