package com.warmup.familytalk.chats;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.UnicastProcessor;

@Slf4j
public class ChatSocketSubscriber {

    private UnicastProcessor<ChatMessage> chatMessagePublisher;

    ChatSocketSubscriber(ChatRoom chatRoom) {
        this.chatMessagePublisher = chatRoom.getEventProcessor();
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
