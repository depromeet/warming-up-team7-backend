package com.warmup.familytalk.register.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import com.warmup.familytalk.register.model.Role;
import com.warmup.familytalk.register.model.User;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.common.LoggingUtils.logOnError;
import static com.warmup.familytalk.common.LoggingUtils.logOnNext;

@Slf4j
@Service
public class UserService {
    private final String adminUsername = "admin001";// password: admin
    private final User admin = new User(1000,
                                        adminUsername,
                                        "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=",
                                        false,
                                        Arrays.asList(Role.ROLE_ADMIN));

    private final String userUsername = "user001";// password: user
    private final User user = new User(1001,
                                       userUsername,
                                       "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=",
                                       true,
                                       Arrays.asList(Role.ROLE_USER));

    public Mono<User> findByUsername(String username) {

        return Flux.just(admin, user)
                   .filter(candidate -> candidate.equalsIn(username))
                   .switchIfEmpty(Mono.error(new IllegalArgumentException("Not found username")))
                   .single()
                   .doOnEach(logOnNext(user -> log.info("hello world")))
                   .doOnEach(logOnError(e -> log.error("fail to find username by [{}], e:[{}]", username, e)));
    }

    public Mono<User> findByUserId(long userId) {
        Mono<User> validUser = Mono.just(user);
        Mono<User> validAdmin = Mono.just(admin);

        return Flux.merge(validUser, validAdmin)
                   .filter(candidate -> candidate.equalsIn(userId))
                   .single();
    }
}
