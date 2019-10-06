package com.warmup.familytalk.study;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.warmup.familytalk.study.StudyController.SUCCESS_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(value = StudyController.class)
@ActiveProfiles("test")
class StudyControllerTest {

    private static Logger log = LoggerFactory.getLogger(StudyControllerTest.class);

    @Autowired
    private WebTestClient webClient;

    @Test
    void test(){
        webClient.get()
                .uri("/study")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(SUCCESS_MESSAGE);
    }

    @Test
    void save(){
        Room responseBody = webClient.post()
                .uri("/study")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Room.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.getId()).isEqualTo(1l);
        assertThat(responseBody.getName()).isEqualTo("room1");
    }

    @Test
    void query() {
        List<Room> responseBody = webClient.get()
                .uri("/studies")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Room.class)
                .hasSize(10)
                .returnResult()
                .getResponseBody();

        log.debug("test : {}", responseBody);
    }
}
