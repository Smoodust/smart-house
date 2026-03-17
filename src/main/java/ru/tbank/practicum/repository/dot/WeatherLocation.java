package ru.tbank.practicum.repository.dot;

public class WeatherLocation {
    private double latitude;
    private double longtitude;
    private double temperature;

    public WeatherLocation(double latitude, double longtitude, double temperature) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.temperature = temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getTemperature() {
        return temperature;
    }
}
