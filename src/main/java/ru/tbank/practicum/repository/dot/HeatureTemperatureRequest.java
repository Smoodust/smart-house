package ru.tbank.practicum.repository.dot;

public class HeatureTemperatureRequest {
    private Long temperature;

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }

    public HeatureTemperatureRequest(Long temperature) {
        this.temperature = temperature;
    }
}
