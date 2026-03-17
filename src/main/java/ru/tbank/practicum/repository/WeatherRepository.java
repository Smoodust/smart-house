package ru.tbank.practicum.repository;

import ru.tbank.practicum.repository.dot.WeatherLocation;

import java.util.List;

public interface WeatherRepository {
    WeatherLocation getNearestWeatherLocation(double lat, double lon);
    List<WeatherLocation> getAllWeatherLocations();
    void updateWeather(WeatherLocation newWeather);
}
