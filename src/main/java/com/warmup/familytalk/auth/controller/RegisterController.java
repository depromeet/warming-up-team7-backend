package com.warmup.familytalk.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.warmup.familytalk.auth.service.PasswordService;
import com.warmup.familytalk.common.Trace;
import com.warmup.familytalk.auth.model.Auth;
import com.warmup.familytalk.auth.service.JwtService;
import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public final class RegisterController {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/login")
    public final Mono<ResponseEntity<Auth.Response>> login(final Trace trace,
                                                           @RequestBody final Auth.Request authRequest) {

        return userService.findByUsername(authRequest.getUsername())
                          .filterWhen(user -> passwordService.matches(authRequest, user))
                          .flatMap(jwtService::generate)
                          .map(Auth.Factory::response)
                          .map(ResponseEntity::ok)
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                          .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public final Mono<ResponseEntity<?>> register(@RequestBody final Auth.RegisterRequest authRequest) {
        passwordService.validate(authRequest);
        userService.createUser(authRequest);
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).build());
    }
}

