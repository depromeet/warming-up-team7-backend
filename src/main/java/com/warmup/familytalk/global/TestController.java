package com.warmup.familytalk.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Charsets;
import com.warmup.familytalk.common.Trace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.common.LoggingUtils.logging;

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
    public Mono<ResponseEntity<String>> file(final Trace trace) {
        return Flux.fromStream(() -> getTestFileStream(trace, "/hello.txt"))
                   .reduce(StringUtils.EMPTY, (a, b) -> a + b)
                   .map(ResponseEntity::ok)
                   .onErrorReturn(ResponseEntity.badRequest().build());
    }

    private Stream<String> getTestFileStream(final Trace trace, final String location) {
        try {
            return new BufferedReader(new InputStreamReader(new ClassPathResource(location).getInputStream(),
                                                            Charsets.UTF_8))
                    .lines();
        } catch (IOException e) {
            logging(trace, () -> log.error("file not found123123123:[{}], e:[{}]", location, e.toString()));

            throw new RuntimeException(e);
        }
    }
}
