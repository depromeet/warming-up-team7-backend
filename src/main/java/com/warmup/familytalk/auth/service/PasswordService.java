package com.warmup.familytalk.auth.service;

import org.springframework.stereotype.Service;
import com.warmup.familytalk.auth.model.Auth;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PBKDF2Encoder passwordEncoder;

    public Mono<Boolean> matches(Auth.Request authRequest, PasswordAware passwordAware) {
        try {
            final String encodedPassword = passwordEncoder.encode(authRequest.getPassword());
            return Mono.just(StringUtils.equals(encodedPassword, passwordAware.getPassword()));
        } catch (RuntimeException e) {
            return Mono.just(false);
        }
    }

    public String encode(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void validate(final Auth.RegisterRequest authRequest) {
        //
    }
}
