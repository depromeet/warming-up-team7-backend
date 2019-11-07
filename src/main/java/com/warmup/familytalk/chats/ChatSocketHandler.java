package com.warmup.familytalk.chats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmup.familytalk.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.Principal;

import static com.warmup.familytalk.chats.ChatConfig.CHAT2_URL;
import static com.warmup.familytalk.chats.ChatRoomManager.CHATROOMS;

@Slf4j
@RequiredArgsConstructor
public class ChatSocketHandler implements WebSocketHandler {

    private final ChatRoomManager chatRoomManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        long roomId = getRoomId(session);
        log.debug("RoomId : {}", roomId);
        ChatRoom chatRoom = CHATROOMS.getChatRoom(roomId);

        ChatSocketSubscriber subscriber = new ChatSocketSubscriber(chatRoom);
        return getUserId(session)
                .flatMap(userId -> {
                    Flux<String> outputMessages = Flux.from(chatRoom.getEvent())
                                                      .map(this::toJSON);
                    chatRoomManager.enter(roomId, userId);

                    session.receive()
                           .map(WebSocketMessage::getPayloadAsText)
                           .map(it -> toChatMessage(it, roomId, userId))
                           .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

                    return session.send(outputMessages.map(session::textMessage));
                });
    }

    private Mono<Long> getUserId(WebSocketSession session) {
        return session.getHandshakeInfo()
                                .getPrincipal()
                                .map(Principal::getName)
                                .map(Long::valueOf);
    }

    private long getRoomId(WebSocketSession session) {
        return Long.parseLong(session.getHandshakeInfo()
                                     .getUri()
                                     .getPath()
                                     .replace(CHAT2_URL, Strings.EMPTY));
    }

    private ChatMessage toChatMessage(String json, long roomId, long userId) {

        try {
            return mapper.readValue(json, ChatMessage.class)
                         .bind(roomId, userId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid JSON:" + json);
        }
    }

    private String toJSON(ChatMessage chatMessage) {
        try {
            return mapper.writeValueAsString(chatMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
