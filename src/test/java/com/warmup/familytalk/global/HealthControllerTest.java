package com.warmup.familytalk.global;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.warmup.familytalk.global.HealthController.SUCCESS_MESSAGE;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(value = HealthController.class)
class HealthControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void test() {
        webClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(SUCCESS_MESSAGE);
    }
}