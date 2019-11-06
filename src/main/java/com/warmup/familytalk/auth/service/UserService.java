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

    public Mono<User> createUser(final Auth.RegisterRequest request) {
        return userRepository.save(toUser(request));
    }

    private User toUser(Auth.RegisterRequest request) {
        final String encodedPassword = passwordService.encode(request.getPassword());
        return new User(-1,
                        request.getUsername(),
                        request.getNickname(),
                        encodedPassword,
                        request.getCountry(),
                        request.getProfileImageNumber(),
                        true,
                        Role.ROLE_USER);
    }
}
