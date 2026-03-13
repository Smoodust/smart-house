package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.HeaterRepository;
import ru.tbank.practicum.repository.dot.Heater;

@Service
public class HeaterService {
    @Autowired
    private HeaterRepository heaterRepository;

    public Heater getHeaterbyId(Long heaterId) {
        return heaterRepository.getHeaterbyId(heaterId);
    }

    public void createHeater(Long heaterId, Long temperature) {
        heaterRepository.createNewHeater(heaterId, temperature);
    }

    public void changeTemperatureSetting(Long heaterId, Long temperature) {
        heaterRepository.changeTemperature(heaterId, temperature);
    }
}
