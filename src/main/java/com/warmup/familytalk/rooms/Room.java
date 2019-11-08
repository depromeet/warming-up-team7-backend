package com.warmup.familytalk.rooms;

import com.warmup.familytalk.auth.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Room {

    private static AtomicLong ID = new AtomicLong();

    private Long id;
    private String name;
    private User owner;
    private Set<Long> users = new ConcurrentSkipListSet<>();
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static Room create(String name, User owner) {
        return Room.builder()
                   .name(name)
                   .owner(owner)
                   .createDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();
    }

    @Builder
    private Room(Long id,
                String name,
                User owner,
                LocalDateTime createDate,
                LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    void populate() {
        id = ID.incrementAndGet();
        createDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }

    void enter(long userId) {
        users.add(userId);
    }

    public boolean isParticipate(long userId) {
        return false;
    }
}
