package com.warmup.familytalk.auth.service;

import com.warmup.familytalk.auth.model.Auth;
import com.warmup.familytalk.auth.model.Role;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public Mono<User> findByUsername(final String username) {
        return userRepository.getUserByUsername(username);
    }

    public Mono<User> findByUserId(final long userId) {
        return userRepository.getUserById(userId);
    }

    public Mono<User> createUser(final Auth.RegisterRequestOne request) {
        return userRepository.save(toUser(request));
    }

    public Mono<User> updateUser(final Auth.RegisterRequestTwo request) {
        return userRepository.getUserByUsername(request.getUsername())
                             .map(user -> {
                                 user.setCountry(request.getCountry());
                                 return user;
                             });
    }

    public Mono<User> updateUser(final Auth.RegisterRequestThree request) {
        return userRepository.getUserByUsername(request.getUsername())
                             .map(user -> {
                                 user.setProfileImageNumber(request.getProfileImageNumber());
                                 return user;
                             });
    }

    private User toUser(Auth.RegisterRequestOne request) {
        final String encodedPassword = passwordService.encode(request.getPassword());
        return new User(-1,
                        request.getUsername(),
                        request.getNickname(),
                        encodedPassword,
                        "kr",
                        1,
                        true,
                        Role.ROLE_USER);
    }
}
