package ru.tbank.practicum.repository.dot;

public class WeatherResponse {
    private double temperature;

    public WeatherResponse(double temperature) {
        this.temperature = temperature;
    }

    public WeatherResponse(WeatherLocation wl) {
        this(wl.getTemperature());
    }
}
