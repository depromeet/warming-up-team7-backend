package com.warmup.familytalk.rooms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private static final Map<Long, Room> ROOMS = new ConcurrentHashMap<>();

    @Override
    public Mono<Room> save(Room room) {
        room.populate();
        ROOMS.put(room.getId(), room);
        return Mono.just(room);
    }

    @Override
    public Mono<Room> findById(long id) {
        return Mono.just(ROOMS.get(id));
    }

    public Mono<Room> findByUserId(long userId) {
        return Mono.justOrEmpty(ROOMS.values()
                                     .stream()
                                     .filter(room -> room.isParticipate(userId))
                                     .findFirst());
    }

    @Override
    public Mono<Room> removeById(long id) {
        return Mono.just(ROOMS.remove(id));
    }
}