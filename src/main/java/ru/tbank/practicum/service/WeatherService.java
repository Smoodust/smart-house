package ru.tbank.practicum.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

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

    public WeatherAPIResponse getWeather(double lat, double lon) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherAPIResponse.class)
                .block();
    }

    public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
        WeatherLocation loc = weatherRepository.getNearestWeatherLocation(lat, lon);
        if (loc != null) {
            return loc;
        }

        Optional<WeatherAPIResponse> weatherResponse = Optional.ofNullable(getWeather(lat, lon));
        Optional<Double> temperature =
                weatherResponse.map(WeatherAPIResponse::main).map(WeatherAPIResponse.Main::temp);
        if (temperature.isEmpty()) {
            return null;
        }
        loc = new WeatherLocation(lat, lon, temperature.get());
        weatherRepository.updateWeather(loc);
        return loc;
    }
}
