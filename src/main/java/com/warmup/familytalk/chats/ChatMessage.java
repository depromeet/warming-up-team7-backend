package com.warmup.familytalk.chats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.auth.model.User;
import lombok.*;

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
            @JsonProperty("eventType") EventType eventType,
            @JsonProperty("contents") String message) {
//            @JsonProperty("sender") Sender sender) {
        return new ChatMessage(0L, eventType, null, messageType, message);
    }

    ChatMessage bind(long roomId, User user) {
        this.roomId = roomId;
        this.sender = new Sender(user.getUserId(), user.getUsername());
        return this;
    }

    boolean isNews() {
        return eventType == EventType.NEWS;
    }

    void setMessage(String message) {
        contents = message;
    }

    enum MessageType {

        TEXT, TECHNOLOGY, SPORTS, ENTERTAINMENT, HEALTH
    }

    enum EventType {

        ENTER, TALK, NEWS
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    static class Sender {

        private Long userId;
        private String name;
    }
}