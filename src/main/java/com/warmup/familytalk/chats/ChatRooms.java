package com.warmup.familytalk.chats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChatRooms {

    private static final ChatRooms INSTANCE = new ChatRooms();

    private static final Map<Long, ChatRoom> ROOMS = new ConcurrentHashMap<>();

    private static final Map<Long, ChatRoom> USERID_TO_ROOM = new ConcurrentHashMap<>();

    static ChatRooms getInstance() {
        return INSTANCE;
    }

    ChatRoom getChatRoom(long roomId) {
        ChatRoom chatRoom = ROOMS.getOrDefault(roomId, ChatRoom.of());
        ROOMS.put(roomId, chatRoom);
        return ROOMS.get(roomId);
    }

    void enter(long roomId, long userId) {
        ChatRoom chatRoom = ROOMS.get(roomId);
        USERID_TO_ROOM.put(userId, chatRoom);
    }

    Mono<ChatRoom> findByUserId(long userId) {
        return Mono.just(USERID_TO_ROOM.get(userId));
    }

    Flux<Long> findParticipants(long userId) {
        return Flux.fromIterable(USERID_TO_ROOM.entrySet()
                                               .stream()
                                               .filter(entry -> USERID_TO_ROOM.get(userId)
                                                                              .equals(entry.getValue()))
                                               .map(Map.Entry::getKey)
                                               .collect(toList()));
    }
}
