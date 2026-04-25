package ru.tbank.practicum.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WebClient webClient;
    private final String apiKey;

    public WeatherService(
            WeatherRepository weatherRepository, WebClient webClient, @Value("${weather.api.key}") String apiKey) {
        this.weatherRepository = weatherRepository;
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

    public WeatherLocation updateWeather(WeatherLocation weatherLocation) {
        Optional<WeatherAPIResponse> weatherResponse =
                Optional.ofNullable(getWeather(weatherLocation.getLatitude(), weatherLocation.getLongtitude()));
        Optional<Double> temperature =
                weatherResponse.map(WeatherAPIResponse::main).map(WeatherAPIResponse.Main::temp);
        if (temperature.isEmpty()) {
            return null;
        }
        weatherLocation.setTemperature(temperature.get());
        return weatherRepository.save(weatherLocation);
    }

    public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
        lat = (double) Math.round(lat * 10) / 10;
        lon = (double) Math.round(lon * 10) / 10;

        Optional<WeatherLocation> loc = weatherRepository.findByLatitudeAndLongtitude(lat, lon);
        if (loc.isPresent()) {
            return loc.get();
        }

        WeatherLocation newLocation = new WeatherLocation();
        newLocation.setLatitude(lat);
        newLocation.setLongtitude(lon);
        return updateWeather(weatherRepository.save(newLocation));
    }
}
