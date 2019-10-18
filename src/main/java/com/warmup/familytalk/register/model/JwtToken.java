package com.warmup.familytalk.register.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtToken {
    private static final ZoneOffset DEFAULT_TIMEZONE_OFFSET = ZoneOffset.UTC;

    private final long userId;
    private final Map<String, Object> claims;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiredAt;

    private final String secret;
    private final SignatureAlgorithm signatureAlgorithm;

    private JwtToken(final long userId,
                     final Map<String, Object> claims,
                     final LocalDateTime createdAt,
                     final LocalDateTime expiredAt,
                     final String secret,
                     final SignatureAlgorithm signatureAlgorithm) {
        this.userId = userId;
        this.claims = claims;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.secret = secret;
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public static JwtToken from(String token, String secret) {
        final Jws<Claims> jws = Jwts.parser()
                                    .setSigningKey(getEncodedSecretKey(secret))
                                    .parseClaimsJws(token);

        return builder()
                .withUserId(Long.parseLong(jws.getBody().getSubject()))
                .withCreatedAt(toLocalDateTime(jws.getBody().getIssuedAt()))
                .withExpiredAt(toLocalDateTime(jws.getBody().getExpiration()))
                .withRoles(Builder.getRoles(jws.getBody()))
                .withSecret(secret)
                .build();
    }

    public boolean isValid() {
        return !isExpired();
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now(DEFAULT_TIMEZONE_OFFSET));
    }

    public long getUserId() {
        return userId;
    }

    public Set<Role> getRoles() {
        return Builder.getRoles(claims);
    }

    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

    public String toTokenString() {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(String.valueOf(userId))
                   .setIssuedAt(toDate(createdAt))
                   .setExpiration(toDate(expiredAt))
                   .signWith(signatureAlgorithm, getEncodedSecretKey(secret))
                   .compact();
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(DEFAULT_TIMEZONE_OFFSET));
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_TIMEZONE_OFFSET);
    }

    private static String getEncodedSecretKey(String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private static final String ROLE_KEY = "role";
        private static final Class<Set> ROLE_VALUE_TYPE = Set.class;

        private long userId;
        private Map<String, Object> claims = new HashMap<>();
        private LocalDateTime createdAt;
        private LocalDateTime expiredAt;
        private String secret;
        private SignatureAlgorithm signatureAlgorithm;

        private Builder() {
        }

        public Builder withUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withExpiredAt(LocalDateTime expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public Builder withSecret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder withSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
            return this;
        }

        public Builder withRoles(Role... roles) {
            withRoles(Arrays.asList(roles));
            return this;
        }

        public Builder withRoles(Collection<Role> roles) {
            claims.put(ROLE_KEY, Collections.unmodifiableCollection(roles));
            return this;
        }

        public JwtToken build() {
            return new JwtToken(userId, claims, createdAt, expiredAt, secret, signatureAlgorithm);
        }

        @SuppressWarnings("unchecked")
        private static Set<Role> getRoles(Map<String, Object> claims) {
            return Collections.unmodifiableSet(ROLE_VALUE_TYPE.cast(claims.get(ROLE_KEY)));
        }
    }
}
