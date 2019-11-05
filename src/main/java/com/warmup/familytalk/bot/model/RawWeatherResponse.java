package com.warmup.familytalk.bot.model;

import java.util.List;

import lombok.Data;

@Data
public class RawWeatherResponse {
    private List<Weather> weather;
    private String name;

    @Data
    public static class Weather {
        private long id;
        private String main;
        private String description;
        private String icon;
    }
}
