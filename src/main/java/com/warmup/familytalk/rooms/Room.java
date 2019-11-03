package com.warmup.familytalk.rooms;

import com.warmup.familytalk.register.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Room {

    private static AtomicLong ID = new AtomicLong();

    private Long id;
    private String name;
    private User owner;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public Room(Long id,
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

    static Room create(String name, User owner) {
        return Room.builder()
                .name(name)
                .owner(owner)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    void populate() {
        id = ID.incrementAndGet();
        createDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }
}
