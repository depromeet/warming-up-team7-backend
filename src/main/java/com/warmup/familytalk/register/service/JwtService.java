package com.warmup.familytalk.register.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.warmup.familytalk.register.model.JwtToken;
import com.warmup.familytalk.register.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class JwtService implements Serializable {
    private static final long serialVersionUID = 3830145016693475178L;
    private static final ZoneOffset DEFAULT_TIMEZONE_OFFSET = ZoneOffset.UTC;
    private static final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private final String secret;
    private final String expirationTimeInMilliSecond;

    @Autowired
    public JwtService(@Value("${jjwt.secret}") final String secret,
                      @Value("${jjwt.expiration}") final String expirationTimeInMilliSecond) {
        this.secret = secret;
        this.expirationTimeInMilliSecond = expirationTimeInMilliSecond;
    }

    public Mono<JwtToken> parse(String token) {
        try {
            return Mono.just(JwtToken.from(token, secret));
        } catch (JwtException e) {
            return Mono.empty();
        }
    }

    public Mono<JwtToken> generate(User user) {
        final long expirationTimeInMilliSecondLong = Long.parseLong(expirationTimeInMilliSecond);

        final LocalDateTime createdAt = LocalDateTime.now(DEFAULT_TIMEZONE_OFFSET);
        final LocalDateTime expiredAt = createdAt.plus(expirationTimeInMilliSecondLong, ChronoUnit.MILLIS);

        try {
            return Mono.just(JwtToken.builder()
                                     .withUserId(Long.parseLong(user.getUsername()))
                                     .withSecret(secret)
                                     .withCreatedAt(createdAt)
                                     .withExpiredAt(expiredAt)
                                     .withSignatureAlgorithm(DEFAULT_SIGNATURE_ALGORITHM)
                                     .build());

        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}