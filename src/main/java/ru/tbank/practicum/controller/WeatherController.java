package ru.tbank.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.service.dto.WeatherResponse;
import ru.tbank.practicum.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public WeatherResponse getWeather(@RequestParam double lat, @RequestParam double lon) {
        return new WeatherResponse(weatherService.getNearestWeatherLocation(lat, lon));
    }
}
