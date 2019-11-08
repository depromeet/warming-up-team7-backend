package com.warmup.familytalk.chats;

import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.UserService;
import com.warmup.familytalk.rooms.Room;
import com.warmup.familytalk.rooms.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomManager {

    static final ChatRooms CHATROOMS = ChatRooms.getInstance();
    private final UserService userService;
    private final RoomRepository roomRepository;

    Mono<Room> findRoomByUserId(long userId) {
        return CHATROOMS.findByUserId(userId)
                        .flatMap(chatRoom -> roomRepository.findById(chatRoom.getId()));
    }

    Flux<User> participants(long userId) {
        return CHATROOMS.findParticipants(userId)
                        .flatMap(userService::findByUserId);
    }

    void enter(long roomId, long userId) {
        CHATROOMS.enter(roomId, userId);
    }


    private static final List<ChatMessage> CHAT_MESSAGES = new CopyOnWriteArrayList<>();

    void saveMessage(ChatMessage message) {
        log.debug("Save message : {}", message);
        CHAT_MESSAGES.add(message);
    }


    Flux<ChatMessage> findHistoryByRoomId(long id) {
        return Flux.fromStream(CHAT_MESSAGES.stream()
                                            .filter(message -> message.getRoomId()
                                                                      .equals(id)));
    }
}
