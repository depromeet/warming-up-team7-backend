package com.warmup.familytalk.rooms;

import com.warmup.familytalk.register.model.Role;
import com.warmup.familytalk.register.model.User;
import com.warmup.familytalk.supports.HandlerSupports;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static com.warmup.familytalk.rooms.RoomRouter.ROOMS_URL;

class CreateRoomHandlerTest extends HandlerSupports {

    @Autowired
    private WebTestClient webTestClient;

    @WithMockUser
    @Test
    void handle() {
        // given
        RoomCreateRequest room = new RoomCreateRequest("room");

        // when && then
        webTestClient.post()
                .uri(ROOMS_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(room), RoomCreateRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}