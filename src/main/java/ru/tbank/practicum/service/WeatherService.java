package ru.tbank.practicum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(WebClient webClient, @Value("${weather.api.key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    private WeatherAPIResponse getWeather(double lat, double lon) {
        return webClient.get()
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

        WeatherAPIResponse weatherResponse = getWeather(lat, lon);
        assert weatherResponse != null;
        loc = new WeatherLocation(lat, lon, weatherResponse.main().temp());
        weatherRepository.updateWeather(loc);
        return loc;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateWeatherLocationInfo() {
        for (WeatherLocation wl : weatherRepository.getAllWeatherLocations()) {
            WeatherAPIResponse weatherResponse = getWeather(wl.getLatitude(), wl.getLongtitude());
            assert weatherResponse != null;
            WeatherLocation newWeather = new WeatherLocation(wl.getLatitude(), wl.getLongtitude(), weatherResponse.main().temp());
            weatherRepository.updateWeather(newWeather);
        }
    }
}
