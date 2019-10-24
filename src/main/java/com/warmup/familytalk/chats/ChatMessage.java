package com.warmup.familytalk.chats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.register.model.User;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
class ChatMessage {

    private Long roomId;
    private EventType eventType;
    private Sender sender;
    private MessageType messageType;
    private String contents;

    @JsonCreator
    static ChatMessage create(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("contents") String message,
            @JsonProperty("sender") Sender sender) {
        return new ChatMessage(0L, EventType.TALK, sender, messageType, message);
    }

    // todo : Request -> token payload
    static ChatMessage join(User user) {
        return ChatMessage.builder()
                .eventType(EventType.ENTER)
                .messageType(MessageType.TEXT)
                .contents("User join")
                .sender(new Sender(0l, user.getUsername()))
                .build();
    }

    enum MessageType {

        IMAGE, TEXT
    }

    enum EventType {

        ENTER, TALK
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @ToString
    static class Sender {

        private Long id;
        private String name;
    }
}