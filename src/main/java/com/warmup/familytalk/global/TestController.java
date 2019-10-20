package com.warmup.familytalk.global;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.io.Files;
import com.warmup.familytalk.common.LogHelper;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity hi() {
        return ResponseEntity.ok("hi");
    }

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/file")
    public Mono<ResponseEntity> file() throws IOException {

        final List<String> strings = Files.readLines(new ClassPathResource("/hello.txt").getFile(), CharsetUtil.UTF_8);
        return Flux.fromStream(strings.stream())
                   .doOnEach(LogHelper.logOnNext(str -> log.info("{}===str", str)))
                   .reduce("", (a, b) -> a + b)
                   .map(ResponseEntity::ok);
    }

}
