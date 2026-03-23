package ru.tbank.practicum.repository;

import java.util.*;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.entity.WeatherLocation;

@Repository
public class MockRepositoryTemp implements WeatherRepository {
    List<WeatherLocation> weatherLocations = new ArrayList<>();

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
        for (WeatherLocation wl : weatherLocations) {
            if (calculateDistance(lat, lon, wl.getLatitude(), wl.getLongtitude()) < 10.0) {
                return wl;
            }
        }
        return null;
    }

    @Override
    public List<WeatherLocation> getAllWeatherLocations() {
        return weatherLocations;
    }

    @Override
    public void updateWeather(WeatherLocation newWeather) {
        for (WeatherLocation wl : weatherLocations) {
            if (calculateDistance(
                            newWeather.getLatitude(), newWeather.getLongtitude(), wl.getLatitude(), wl.getLongtitude())
                    < 10.0) {
                wl.setTemperature(newWeather.getTemperature());
                return;
            }
        }
        weatherLocations.add(newWeather);
    }
}
