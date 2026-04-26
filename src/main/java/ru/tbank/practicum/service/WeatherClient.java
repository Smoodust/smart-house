package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@Service
public class WeatherClient {
    private final RestClient restClient;
    private final String apiKey;

    public WeatherClient(
            RestClient restClient,
            @Value("${weather.api.key}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public WeatherAPIResponse getWeather(double lat, double lon) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .body(WeatherAPIResponse.class);
    }
}
