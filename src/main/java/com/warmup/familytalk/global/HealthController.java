package com.warmup.familytalk.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {
    @GetMapping("/l7_health_check")
    public Mono<ResponseEntity> health() {
        return Mono.just(ResponseEntity.ok().build());
    }
}