package com.warmup.familytalk.auth;

import com.warmup.familytalk.auth.model.Auth;
import com.warmup.familytalk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        Auth.RegisterRequest user = new Auth.RegisterRequest("user",
                                                             "password",
                                                             "password",
                                                             "EN");
        userService.createUser(user)
                   .subscribe(it -> log.debug("Create user: {}", it));
    }
}
