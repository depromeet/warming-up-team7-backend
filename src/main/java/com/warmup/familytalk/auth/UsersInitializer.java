package com.warmup.familytalk.auth;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.warmup.familytalk.auth.model.Auth;
import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsersInitializer implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        Flux<Auth.RegisterRequestOne> user = Flux.just(
                new Auth.RegisterRequestOne("user",
                                            "user-",
                                            "password",
                                            "password"),
                new Auth.RegisterRequestOne("testuser1",
                                            "testuser1-",
                                            "password",
                                            "password"
                ),
                new Auth.RegisterRequestOne("testuser2",
                                            "testuser2-",
                                            "password",
                                            "password"));
        user.map(userService::createUser)
            .subscribe(it -> log.debug("Create user: {}",
                                       it));
    }
}
