package com.warmup.familytalk.chats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChatManager {

    private static final List<ChatMessage> CHAT_MESSAGES = new ArrayList<>();

    void saveMessage(ChatMessage message) {
        log.debug("Save message : {}", message);
        CHAT_MESSAGES.add(message);
    }

    Flux<ChatMessage> findHistoryByRoomId(long id) {
        CHAT_MESSAGES.stream()
                .filter(message -> message.getRoomId().equals(id));
        return null;
    }
}
