package com.warmup.familytalk.auth;

import java.util.Arrays;
import java.util.List;

import com.warmup.familytalk.auth.model.Auth;
import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsersInitializer implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        Flux<Auth.RegisterRequest> user = Flux.just(
                new Auth.RegisterRequest("user",
                                         "password",
                                         "password",
                                         "kr",
                                         "1",
                                         "EN"),
                new Auth.RegisterRequest("testuser1",
                                         "password",
                                         "password",
                                         "kr",
                                         "2",
                                         "EN"),
                new Auth.RegisterRequest("testuser2",
                                         "password",
                                         "password",
                                         "kr",
                                         "3",
                                         "EN"));
        user.map(userService::createUser)
            .subscribe(it -> log.debug("Create user: {}", it));
    }
}
