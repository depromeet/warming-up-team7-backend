package com.warmup.familytalk.chats;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class ChatSocketSubscriber {

    private ChatRoomManager chatRoomManager;
    private UnicastProcessor<ChatMessage> chatMessagePublisher;

    ChatSocketSubscriber(ChatRoom chatRoom) {
//        this.chatRoom = chatRoom;
        this.chatMessagePublisher = chatRoom.getEventProcessor();
    }

    void onNext(Mono<ChatMessage> chatMessage) {
        log.debug("{}", chatMessage);
//        lastReceivedMessage = Optional.of(chatMessage);
        chatMessagePublisher.onNext(chatMessage.block());
    }

    void onNext(ChatMessage chatMessage) {
        log.debug("{}", chatMessage);
        chatMessagePublisher.onNext(chatMessage);
    }

    void onError(Throwable error) {
        log.debug("{}", error);
    }

    void onComplete() {
        // todo : session disconnected
//        chatRoom.leave();
    }
}
