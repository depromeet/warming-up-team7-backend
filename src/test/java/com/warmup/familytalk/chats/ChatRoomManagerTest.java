package com.warmup.familytalk.chats;

import com.warmup.familytalk.rooms.InMemoryRoomRepository;
import com.warmup.familytalk.rooms.Room;
import com.warmup.familytalk.rooms.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.chats.ChatRoomManager.CHATROOMS;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ChatRoomManagerTest {

    private ChatRoomManager chatRoomManager;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository = new InMemoryRoomRepository();
        roomRepository.save(Room.create("room1", null));
        chatRoomManager = new ChatRoomManager(roomRepository);
    }

    @Test
    void createRoom() {
        long roomId = 1l;
        long userId = 2l;
        CHATROOMS.getChatRoom(roomId);
        chatRoomManager.enter(roomId, userId);

        Long createRoomId = roomRepository.findById(roomId)
                                          .block()
                                          .getId();

        assertThat(createRoomId).isEqualTo(roomId);
    }

    @Test
    void findRoomByUserId() {
        long roomId = 1l;
        long userId = 2l;
        CHATROOMS.getChatRoom(roomId);
        chatRoomManager.enter(roomId, userId);

        Room result = chatRoomManager.findRoomByUserId(userId)
                                    .block();

        assertThat(result.getId()).isEqualTo(roomId);
    }
}