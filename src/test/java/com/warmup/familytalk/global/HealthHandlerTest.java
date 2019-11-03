package com.warmup.familytalk.global;

import com.warmup.familytalk.supports.HandlerSupports;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.warmup.familytalk.global.HealthHandler.SUCCESS_MESSAGE;
import static com.warmup.familytalk.global.HealthRouter.HEALTH_URL;

class HealthHandlerTest extends HandlerSupports {

    @Autowired
    private WebTestClient webClient;

    @WithMockUser
    @Test
    void handle() {
        webClient.get()
                .uri(HEALTH_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(SUCCESS_MESSAGE);
    }
}