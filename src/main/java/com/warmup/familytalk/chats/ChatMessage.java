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
    private Type type;
    private Sender sender;
    private MessageType messageType;
    private String message;

    @JsonCreator
    static ChatMessage create(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("message") String message,
            @JsonProperty("sender") Sender sender) {
        return new ChatMessage(0l, Type.TALK, sender, messageType, message);
    }

    // todo : Request -> token payload
    static ChatMessage join(User user) {
        return ChatMessage.builder()
                .type(Type.ENTER)
                .messageType(MessageType.TEXT)
                .message("User join")
                .sender(new Sender(0l, user.getUsername()))
                .build();
    }

    enum MessageType {

        IMAGE, TEXT
    }

    enum Type {

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