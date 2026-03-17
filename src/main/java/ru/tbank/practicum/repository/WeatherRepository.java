package ru.tbank.practicum.repository;

import ru.tbank.practicum.repository.dot.WeatherLocation;

public interface WeatherRepository {
    WeatherLocation getNearestWeatherLocation(double lat, double lon);
    void updateWeather(WeatherLocation newWeather);
}
