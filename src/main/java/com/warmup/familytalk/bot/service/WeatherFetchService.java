package com.warmup.familytalk.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.warmup.familytalk.bot.model.Country;
import com.warmup.familytalk.bot.model.RawWeatherResponse;
import com.warmup.familytalk.bot.model.WeatherResponse;
import reactor.core.publisher.Mono;

@Service
public class WeatherFetchService {
    @Value("${api.weather.token}")
    private String appId;

    public Mono<WeatherResponse> fetch(final Country country, final String city) {
        return WebClient.create()
                        .get()
                        .uri("http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={appid}&lang=kr",
                             city, country, appId)
                        .exchange()
                        .flatMap(res -> res.bodyToMono(RawWeatherResponse.class))
                        .map(this::convert);
    }

    private WeatherResponse convert(final RawWeatherResponse raw) {
        final WeatherResponse response = new WeatherResponse();

        response.setDescription(raw.getWeather().get(0).getDescription());
        response.setName(raw.getName());
        response.setIconUrl(toIconUrl(raw.getWeather().get(0).getIcon()));

        return response;
    }

    private String toIconUrl(final String iconName) {
        return String.format("http://openweathermap.org/img/wn/%s@2x.png", iconName);
    }
}
