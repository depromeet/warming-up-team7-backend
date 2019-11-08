package com.warmup.familytalk.chats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.UserService;
import com.warmup.familytalk.bot.service.NewsGeneratorService;
import com.warmup.familytalk.bot.service.NewsGeneratorService.Category;
import com.warmup.familytalk.bot.service.WeatherFetchService;
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
    private final WeatherFetchService weatherFetchService;

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
                           .map(it -> toChatMessage(it, roomId, user))
                           .flatMap(it -> {
                               if (it.isNews()) {
                                   return toNewsMessage(it, user);
                               }
                               if (it.isWeather()) {
                                   return toWeatherMessage(it);
                               }
                               return Mono.just(it);
                           })
                           .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

                    return session.send(outputMessages.map(session::textMessage));
                });
    }

    private Mono<ChatMessage> toWeatherMessage(ChatMessage message) {
        return weatherFetchService.fetch(message.getCity())
                                  .map(weatherResponse -> {
                                      try {
                                          return mapper.writeValueAsString(weatherResponse);
                                      } catch (JsonProcessingException e) {
                                          e.printStackTrace();
                                          throw new RuntimeException(e);
                                      }
                                  })
                                  .map(message::setMessage);
    }

    private Mono<ChatMessage> toNewsMessage(ChatMessage message, User user) {
        return newsGeneratorService.fetchRandomNews(user.getCountry(),
                                                    Category.valueOf(message.getMessageType()
                                                                            .toString()))
                                   .map(botNewsResponse -> {
                                       try {
                                           return mapper.writeValueAsString(botNewsResponse);
                                       } catch (JsonProcessingException e) {
                                           e.printStackTrace();
                                           throw new RuntimeException(e);
                                       }
                                   })
                                   .map(message::setMessage);
    }
/*

    private Mono<User> getUser(WebSocketSession session) {
        return userService.findByUserId(1l);
    }
*/

    private Mono<User> getUser(WebSocketSession session) {
        return session.getHandshakeInfo()
                      .getPrincipal()
                      .map(Principal::getName)
                      .map(Long::valueOf)
                      .flatMap(userService::findByUserId);
    }

    private long getRoomId(WebSocketSession session) {
        return Long.parseLong(session.getHandshakeInfo()
                                     .getUri()
                                     .getPath()
                                     .replace(CHAT_URL, Strings.EMPTY));
    }

    private ChatMessage toChatMessage(String json, long roomId, User user) {
        try {
            return mapper.readValue(json, ChatMessage.class)
                         .bind(roomId, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
