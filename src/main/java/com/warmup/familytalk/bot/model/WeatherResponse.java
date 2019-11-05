package com.warmup.familytalk.bot.model;

import lombok.Data;

@Data
public class WeatherResponse {
    private String name;
    private String description;
    private String iconUrl;
}
