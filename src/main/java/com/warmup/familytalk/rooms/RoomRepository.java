package com.warmup.familytalk.rooms;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoomRepository {

    Mono<Room> save(Room room);

    Mono<Room> findById(long id);

    Mono<Room> removeById(long id);

    Mono<Room> findByUserId(long userId);
}
