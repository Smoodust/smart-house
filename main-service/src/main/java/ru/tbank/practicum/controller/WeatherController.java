package ru.tbank.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.service.WeatherService;
import ru.tbank.practicum.service.dto.WeatherResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {
  private final WeatherService weatherService;

  @GetMapping
  public WeatherResponse getWeather(@RequestParam double lat, @RequestParam double lon) {
    return new WeatherResponse(weatherService.getNearestWeatherLocation(lat, lon));
  }
}
