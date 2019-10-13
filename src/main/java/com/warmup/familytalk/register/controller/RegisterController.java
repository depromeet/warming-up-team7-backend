package com.warmup.familytalk.register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.warmup.familytalk.auth.service.PasswordService;
import com.warmup.familytalk.common.Loggable;
import com.warmup.familytalk.register.model.Auth;
import com.warmup.familytalk.register.service.JwtService;
import com.warmup.familytalk.register.service.UserService;
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
    public final Mono<ResponseEntity<Auth.Response>> login(final Loggable loggable,
                                                           @RequestBody final Auth.Request authRequest) {

        log.debug("{}> start login", loggable);
        return userService.findByUsername(authRequest.getUsername())
                          .filterWhen(user -> passwordService.matches(authRequest, user))
                          .flatMap(jwtService::generate)
                          .map(Auth.Factory::response)
                          .map(ResponseEntity::ok)
                          .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public final Mono<ResponseEntity<?>> register(@RequestBody final Auth.RegisterRequest authRequest) {
        // TODO: implements
        passwordService.validate(authRequest);
        return Mono.just(ResponseEntity.ok().build());
    }
}

