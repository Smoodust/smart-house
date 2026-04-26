package ru.tbank.practicum.service.dto;

import lombok.AllArgsConstructor;
import ru.tbank.practicum.repository.entity.WeatherLocation;

@AllArgsConstructor
public class WeatherResponse {
  private double temperature;

  public WeatherResponse(WeatherLocation wl) {
    this(wl.getTemperature());
  }
}
