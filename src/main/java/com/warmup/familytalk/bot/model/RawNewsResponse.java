package com.warmup.familytalk.bot.model;

import java.util.List;

import lombok.Data;

@Data
public class RawNewsResponse {
    private String status;
    private long totalResults;
    private List<Articles> articles;

    @Data
    public static class Articles {
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private String publishedAt;
        private String content;

        @Data
        public static class Source {
            private String id;
            private String name;
        }
    }
}
