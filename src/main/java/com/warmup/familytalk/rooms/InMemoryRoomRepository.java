package com.warmup.familytalk.rooms;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private static final List<Room> ROOMS = new CopyOnWriteArrayList<>();

    @Override
    public Mono<Room> save(Room room) {
        room.create();
        ROOMS.add(room);
        return Mono.just(room);
    }
}