package com.warmup.familytalk.rooms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    Mono<Room> find(long id) {
        return roomRepository.findById(id);
    }

    Mono<Room> create(Room room) {
        return roomRepository.save(room);
    }

    Mono<Room> remove(long id) {
        return roomRepository.findById(id)
                .flatMap(it -> roomRepository.removeById(it.getId())
                        .thenReturn(it));
    }
}
