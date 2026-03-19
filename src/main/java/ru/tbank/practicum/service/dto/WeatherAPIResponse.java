package ru.tbank.practicum.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WeatherAPIResponse(
        Coord coord,
        List<Weather> weather,
        Main main,
        Wind wind,
        Sys sys,
        String name
) {
    public record Coord(double lon, double lat) {}
    public record Weather(int id, String main, String description, String icon) {}
    public record Main(
            double temp,
            @JsonProperty("feels_like") double feelsLike,
            @JsonProperty("temp_min") double tempMin,
            @JsonProperty("temp_max") double tempMax,
            int pressure,
            int humidity
    ) {}
    public record Wind(double speed, int deg, Double gust) {}
    public record Sys(String country, long sunrise, long sunset) {}
}