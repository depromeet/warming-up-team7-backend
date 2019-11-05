package com.warmup.familytalk.chats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChatRooms {

    private static final ChatRooms INSTANCE = new ChatRooms();

    private static final Map<Long, ChatRoom> ID_TO_ROOMS = new ConcurrentHashMap<>();

    static ChatRooms getInstance() {
        return INSTANCE;
    }

    ChatRoom enter(long roomId) {
        ChatRoom chatRoom = ID_TO_ROOMS.getOrDefault(roomId, ChatRoom.of());
        ID_TO_ROOMS.put(roomId, chatRoom);
        return ID_TO_ROOMS.get(roomId);
    }
}
