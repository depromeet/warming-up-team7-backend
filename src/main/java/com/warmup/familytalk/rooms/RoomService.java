package com.warmup.familytalk.rooms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    Mono<Room> create(Room room) {
        return roomRepository.save(room);
    }

    // 방 조회 (in: userById, out: Optional<Room>


    // 방 삭제
}
