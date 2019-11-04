package com.warmup.familytalk.chats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.warmup.familytalk.chats.ChatConfig.CHAT_URL;

@Slf4j
public class ChatSocketHandler implements WebSocketHandler {

    private static final ChatRooms ROOMS = ChatRooms.getInstance();
    private static final ChatManager CHAT_MANAGER = new ChatManager();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.debug("handle : {}, {}", session.getId(), session.getHandshakeInfo());

        long roomId = getRoomId(session);
        log.debug("RoomId : {}", roomId);
        ChatRoom chatRoom = ROOMS.enter(roomId);

        // todo : User info header
//        Account user = (Account) session.getAttributes().get("user");
//        chatRoom.join(user);

        Flux<String> outputMessages = Flux.from(chatRoom.getEvent())
                .map(this::toJSON);

        ChatSocketSubscriber subscriber = new ChatSocketSubscriber(chatRoom, CHAT_MANAGER);
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(inputOfData -> this.toChatMessage(inputOfData, roomId))
                .subscribe(subscriber::onNext, subscriber::onError, session::close);

        return session.send(outputMessages.map(session::textMessage));
    }

    private long getRoomId(WebSocketSession session) {
        return Long.parseLong(session.getHandshakeInfo()
                .getUri()
                .getPath()
                .replace(CHAT_URL, Strings.EMPTY));
    }

    private ChatMessage toChatMessage(String json, long roomId) {
        try {
            return mapper.readValue(json, ChatMessage.class)
                                            .bind(roomId);
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
