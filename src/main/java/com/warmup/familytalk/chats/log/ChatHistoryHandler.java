//package com.warmup.familytalk.chats.log;
//
//import com.warmup.familytalk.chats.ChatManager;
//import com.warmup.familytalk.chats.ChatMessage;
//import com.warmup.familytalk.global.Handler;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class ChatHistoryHandler implements Handler {
//
//    private final ChatManager chatManager;
//
//    @Override
//    public Mono<ServerResponse> handle(ServerRequest request) {
//        long id = Long.parseLong(request.pathVariable("id"));
//        Flux<ChatMessage> chatMessages = chatManager.findHistoryByRoomId(id)
//                                                    .switchIfEmpty(Flux.empty());
//        return ServerResponse.ok()
//                      .body(BodyInserters.fromPublisher(chatMessages, ChatMessage.class));
//    }
//}
