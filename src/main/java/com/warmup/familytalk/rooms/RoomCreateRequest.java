package com.warmup.familytalk.rooms;

import com.warmup.familytalk.register.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
class RoomCreateRequest {

    private String name;
    private User owner;

    Room toEntity() {
        return Room.builder()
                .name(name)
                .owner(owner)
                .build();
    }
}
