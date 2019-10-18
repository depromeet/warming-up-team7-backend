package com.warmup.familytalk.register.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.warmup.familytalk.common.Loggable;
import com.warmup.familytalk.register.model.Message;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<?>> user() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> admin(Loggable loggable) {
        log.debug("{} - ???????1234", loggable);
        return Mono.just(ResponseEntity.ok(new Message("Content for admin")));
    }

    @GetMapping("/user-or-admin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> userOrAdmin() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('TEST')")
    public Mono<ResponseEntity<?>> test() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
    }
}
