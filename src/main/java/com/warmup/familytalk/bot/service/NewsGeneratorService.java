package com.warmup.familytalk.bot.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.warmup.familytalk.bot.model.BotNewsResponse;
import com.warmup.familytalk.bot.model.Country;
import com.warmup.familytalk.bot.model.RawNewsResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class NewsGeneratorService {

    @Value("${api.news.token}")
    private String newsApiToken;

    private static final String endpoint = "https://newsapi.org/v2/top-headlines";

    public Mono<BotNewsResponse> fetchRandomNews(final Country country, final Category category) {
        return WebClient.create(uri(country, category))
                        .get()
                        .exchange()
                        .flatMap(res -> res.bodyToMono(RawNewsResponse.class))
                        .map(BotNewsResponse::of);
    }

    private String uri(final Country country, final Category category) {
        return String.format("%s?apiKey=%s&country=%s&category=%s&pageSize=10",
                             endpoint,
                             newsApiToken,
                             country.name().toLowerCase(),
                             category.name().toLowerCase());
    }

    public enum Category {
        TECHNOLOGY,
        SPORTS,
        ENTERTAINMENT,
        HEALTH
    }
}
