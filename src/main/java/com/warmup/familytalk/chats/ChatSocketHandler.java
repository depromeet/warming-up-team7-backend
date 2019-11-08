package com.warmup.familytalk.chats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.UserService;
import com.warmup.familytalk.bot.service.NewsGeneratorService;
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

import static com.warmup.familytalk.chats.ChatConfig.CHAT_URL;
import static com.warmup.familytalk.chats.ChatRoomManager.CHATROOMS;

@Slf4j
@RequiredArgsConstructor
public class ChatSocketHandler implements WebSocketHandler {

    private final ChatRoomManager chatRoomManager;
    private final UserService userService;
    private final NewsGeneratorService newsGeneratorService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        long roomId = getRoomId(session);
        log.debug("RoomId : {}", roomId);
        ChatRoom chatRoom = CHATROOMS.getChatRoom(roomId);

        return getUser(session)
                .flatMap(user -> {
                    ChatSocketSubscriber subscriber = new ChatSocketSubscriber(chatRoom);
                    Flux<String> outputMessages = Flux.from(chatRoom.getEvent())
                                                      .map(this::toJSON);

                    chatRoomManager.enter(roomId, user.getUserId());

                    session.receive()
                           .map(WebSocketMessage::getPayloadAsText)
                           .flatMap(it -> toChatMessage(it, roomId, user))
                           .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

                    return session.send(outputMessages.map(session::textMessage));
                });
    }

    private Mono<User> getUser(WebSocketSession session) {
        return userService.findByUserId(1l);
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
                                     .replace(CHAT_URL, Strings.EMPTY));
    }

    private Mono<ChatMessage> toChatMessage(String json, long roomId, User user) {
        try {
            return newsGeneratorService.fetchRandomNews(user.getCountry(),
                                                        NewsGeneratorService.Category.SPORTS)
                                       .map(botNewsResponse -> {
                                           try {
                                               return mapper.writeValueAsString(botNewsResponse);
                                           } catch (JsonProcessingException e) {
                                               e.printStackTrace();
                                               throw new RuntimeException(e);
                                           }
                                       })
                                       .map(str -> {
                                           ChatMessage message = null;


                                           try {
                                               message = mapper.readValue(json, ChatMessage.class);
                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                           if (message != null) {
                                               message.bind(roomId, user);
                                               message.setMessage(str);
                                           }
                                           return message;
                                       });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }

    private String toJSON(ChatMessage chatMessage) {
        try {
            return mapper.writeValueAsString(chatMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
