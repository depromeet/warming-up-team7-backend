package com.warmup.familytalk.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public final Mono<ResponseEntity<User.Response>> login(@PathVariable final long id) {
        return User.Response.of(userService.findByUserId(id)).map(ResponseEntity::ok);
    }
}
