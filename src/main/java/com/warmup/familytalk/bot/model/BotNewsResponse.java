package com.warmup.familytalk.bot.model;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;

@Data
public class BotNewsResponse {
    private String title;
    private String content;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public static BotNewsResponse of(final RawNewsResponse raw) {
        final BotNewsResponse response = new BotNewsResponse();
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        if (raw.getArticles().size() <= 0) {
            return response;
        }

        final int randomIndex = random.nextInt(0, raw.getArticles().size());
        final RawNewsResponse.Articles articles = raw.getArticles().get(randomIndex);
        response.setTitle(articles.getTitle());
        response.setContent(articles.getDescription());
        response.setUrl(articles.getUrl());
        response.setUrlToImage(articles.getUrlToImage());
        response.setPublishedAt(articles.getPublishedAt());

        return response;
    }
}
