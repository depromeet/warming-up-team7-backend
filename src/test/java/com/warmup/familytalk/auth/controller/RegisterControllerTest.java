package com.warmup.familytalk.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @WithMockUser
    void login() {
        webClient.get()
                 .uri("/resources/user")
                 .exchange()
                 .expectStatus().isOk();
    }

    @Test
    @WithMockUser(roles = {"TEST"})
    void login2() {
        webClient.get()
                 .uri("/resources/user-or-admin")
                 .exchange()
                 .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(roles = {"TEST"})
    void login3() {
        webClient.get()
                 .uri("/resources/test")
                 .exchange()
                 .expectStatus().isOk();
    }
}