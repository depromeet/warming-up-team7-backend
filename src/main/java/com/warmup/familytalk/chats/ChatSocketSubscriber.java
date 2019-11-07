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
//    private Optional<ChatMessage> lastReceivedMessage = Optional.empty();


    ChatSocketSubscriber(ChatRoom chatRoom) {
//        this.chatRoom = chatRoom;
        this.chatMessagePublisher = chatRoom.getEventProcessor();
    }
/*
    void join(ChatMessage chatMessage){
        lastReceivedMessage = Optional.of(chatMessage);
    }*/

    void onNext(ChatMessage chatMessage) {
        log.debug("{}", chatMessage);
//        lastReceivedMessage = Optional.of(chatMessage);
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
