package com.warmup.familytalk.auth.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;
import com.warmup.familytalk.auth.model.User;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {
    private AtomicLong userIdCount = new AtomicLong(0);
    private final Map<Long, User> userMapById = new ConcurrentHashMap<>();
    private final Map<String, User> userMapByName = new ConcurrentHashMap<>();

    public Mono<User> getUserById(final long id) {
        return Mono.just(userMapById.get(id));
    }

    public Mono<User> getUserByUsername(final String username) {
        return Mono.just(userMapByName.get(username));
    }

    public Mono<User> save(final User user) {
        user.setUserId(userIdCount.getAndIncrement());
        userMapByName.put(user.getUsername(), user);
        userMapById.put(user.getUserId(), user);
        return Mono.just(user);
    }

    public void update(final User user) {
        userMapByName.put(user.getUsername(), user);
        userMapById.put(user.getUserId(), user);
    }
}