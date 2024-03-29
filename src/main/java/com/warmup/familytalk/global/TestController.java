package com.warmup.familytalk.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Charsets;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.bot.model.BotNewsResponse;
import com.warmup.familytalk.bot.model.WeatherResponse;
import com.warmup.familytalk.bot.service.NewsGeneratorService;
import com.warmup.familytalk.bot.service.WeatherFetchService;
import com.warmup.familytalk.common.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.common.LoggingUtils.dumpThrowable;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final NewsGeneratorService newsGeneratorService;
    private final WeatherFetchService weatherFetchService;

    @GetMapping
    public ResponseEntity hi() {
        return ResponseEntity.ok("hi");
    }

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/withCredential")
    public ResponseEntity withUserCredential(User user) {
        System.out.println(user);
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/file")
    public Mono<ResponseEntity<String>> file(final Trace trace) {
        return Flux.fromStream(() -> getTestFileStream(trace, "/hello1.txt"))
                   .reduce(StringUtils.EMPTY, (a, b) -> a + b)
                   .map(ResponseEntity::ok)
                   .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/news")
    public Mono<ResponseEntity<BotNewsResponse>> news(@RequestParam(name = "country") final String country,
                                                      @RequestParam(name = "category") final NewsGeneratorService.Category category) {
        return newsGeneratorService.fetchRandomNews(country, category)
                                   .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/weather")
    public Mono<ResponseEntity<WeatherResponse>> weather(@RequestParam(name = "city") final String city) {
        return weatherFetchService.fetch(city)
                                  .map(ResponseEntity::ok);
    }

    private Stream<String> getTestFileStream(final Trace trace, final String location) {
        try {
            return new BufferedReader(new InputStreamReader(new ClassPathResource(location).getInputStream(),
                                                            Charsets.UTF_8))
                    .lines();
        } catch (IOException e) {
            dumpThrowable(trace, e);
            return Stream.empty();
        }
    }
}
