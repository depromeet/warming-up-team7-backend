package com.warmup.familytalk.rooms;

import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.UserService;
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

    Mono<Room> create(RoomCreateRequest request, Mono<User> user) {
        return user.flatMap(it -> roomRepository.save(Room.create(request.getName(), it)));
    }

    Mono<Room> remove(long id) {
        return roomRepository.findById(id)
                             .flatMap(it -> roomRepository.removeById(it.getId())
                                                          .thenReturn(it));
    }
}
