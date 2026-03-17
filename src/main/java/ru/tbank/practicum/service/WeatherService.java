package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.dot.WeatherLocation;
import ru.tbank.practicum.repository.dot.WeatherAPIResponse;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;

    private final WebClient webClient;
    private final String apiKey;

    public WeatherService(WebClient webClient, @Value("${weather.api.key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    private Mono<WeatherAPIResponse> getWeatherRequest(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherAPIResponse.class);
    }

    public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
        WeatherLocation loc = weatherRepository.getNearestWeatherLocation(lat, lon);
        if (loc != null) {
            return loc;
        }

        WeatherAPIResponse weatherResponse = getWeatherRequest(lat, lon).block();
        loc = new WeatherLocation(lat, lon, weatherResponse.main().temp());
        weatherRepository.updateWeather(loc);
        return loc;
    }
}
