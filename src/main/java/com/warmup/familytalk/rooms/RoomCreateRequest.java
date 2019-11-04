package com.warmup.familytalk.rooms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomCreateRequest {

    private String name;
//    private User owner;

    Room toEntity() {
        return Room.builder()
                .name(name)
//                .owner(owner)
                .build();
    }
}