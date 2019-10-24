package com.warmup.familytalk.rooms;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private static final Map<Long, Room> ROOMS = new ConcurrentHashMap<>();

    @Override
    public Mono<Room> save(Room room) {
        room.populate();
        ROOMS.put(room.getId(), room);
        return Mono.just(room);
    }
}