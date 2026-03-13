package ru.tbank.practicum.repository.dot;

public class HeaterRequest {
    Long heaterId;
    Long temperature;

    public Long getHeaterId() {
        return heaterId;
    }

    public void setHeaterId(Long heaterId) {
        this.heaterId = heaterId;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }

    public HeaterRequest(Long heaterId, Long temperature) {
        this.heaterId = heaterId;
        this.temperature = temperature;
    }
}
