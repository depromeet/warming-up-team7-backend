package com.warmup.familytalk.chats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.auth.model.User;
import lombok.*;
import reactor.core.publisher.Mono;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
class ChatMessage {

    private Long roomId;
    private EventType eventType;
    private Sender sender;
    private MessageType messageType; // NEWS
    private String contents;

    @JsonCreator
    static ChatMessage create(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("contents") String message) {
//            @JsonProperty("sender") Sender sender) {
        return new ChatMessage(0L, EventType.TALK, null, messageType, message);
    }

    ChatMessage bind(long roomId, long userId) {
        this.roomId = roomId;
        this.sender = new Sender(userId);
        return this;
    }

    enum MessageType {

        IMAGE, TEXT // NEWS
    }

    enum EventType {

        ENTER, TALK
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    static class Sender {

        private Long id;
    }
}