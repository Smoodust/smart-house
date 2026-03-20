package ru.tbank.practicum.repository;

import java.util.List;
import ru.tbank.practicum.repository.entity.WeatherLocation;

public interface WeatherRepository {
    WeatherLocation getNearestWeatherLocation(double lat, double lon);

    List<WeatherLocation> getAllWeatherLocations();

    void updateWeather(WeatherLocation newWeather);
}
