package ru.tbank.practicum.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@Service
public class WeatherScheduler {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateWeatherLocationInfo() {
        for (WeatherLocation wl : weatherRepository.getAllWeatherLocations()) {
            Optional<WeatherAPIResponse> weatherResponse =
                    Optional.ofNullable(weatherService.getWeather(wl.getLatitude(), wl.getLongtitude()));
            weatherResponse
                    .map(WeatherAPIResponse::main)
                    .map(WeatherAPIResponse.Main::temp)
                    .ifPresent(temp -> {
                        WeatherLocation newWeather = new WeatherLocation(wl.getLatitude(), wl.getLongtitude(), temp);
                        weatherRepository.updateWeather(newWeather);
                    });
        }
    }
}
