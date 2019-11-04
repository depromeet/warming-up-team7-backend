package com.warmup.familytalk.chats;

import com.warmup.familytalk.auth.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@Getter
@ToString
class ChatRoom {

    private static final AtomicLong ROOM_ID = new AtomicLong();

    private Long id;
    private UnicastProcessor<ChatMessage> eventProcessor = UnicastProcessor.create();
    private Flux<ChatMessage> event = eventProcessor.replay(25)
            .autoConnect(1);

    private ChatRoom(Long id) {
        this.id = id;
    }

    static ChatRoom of() {
        return new ChatRoom(ROOM_ID.incrementAndGet());
    }

    // todo : Token payload -> user connected
    void join(User user) {
        eventProcessor.onNext(ChatMessage.join(user));
    }
}
