package ru.tbank.practicum.repository;

import ru.tbank.practicum.repository.dot.Heater;

public interface HeaterRepository {
    Heater getHeaterbyId(Long heaterId);
    void createNewHeater(Long heaterId, Long temperature);
    void changeTemperature(Long heaterId, Long temperature);
}
