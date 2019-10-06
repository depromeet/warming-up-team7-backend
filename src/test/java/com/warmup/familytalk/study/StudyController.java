package com.warmup.familytalk.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class StudyController {

    static final String SUCCESS_MESSAGE = "SUCCESS";

    @GetMapping("/study")
    public Mono<String> study(){
        return Mono.just(SUCCESS_MESSAGE);
    }

    @PostMapping("/study")
    public Mono<Room> save(){
        Room newRoom = new Room(1l, "room1");
        return Mono.just(newRoom);
    }

    @GetMapping("/studies")
    public Flux<Room> query(){
        List<Room> rooms = IntStream.rangeClosed(1, 10)
                .mapToObj(num -> new Room((long) num, "room" + num))
                .collect(Collectors.toList());
        return Flux.fromIterable(rooms);
    }
}