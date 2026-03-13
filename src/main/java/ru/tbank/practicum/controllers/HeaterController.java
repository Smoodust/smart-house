package ru.tbank.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.exceptions.HeaterNotFoundException;
import ru.tbank.practicum.repository.dot.Heater;
import ru.tbank.practicum.repository.dot.HeaterRequest;
import ru.tbank.practicum.repository.dot.HeatureTemperatureRequest;
import ru.tbank.practicum.service.HeaterService;

@RestController
@RequestMapping("/heater")
public class HeaterController {
    @Autowired
    private HeaterService heaterService;

    @GetMapping("/{heaterId}/temperature")
    Long getHeaterTemperature(@PathVariable Long heaterId) {
        Heater currentHeater = heaterService.getHeaterbyId(heaterId);
        if (currentHeater == null) {
            throw new HeaterNotFoundException("Heater not found with id " + heaterId);
        }
        return currentHeater.getTemperature();
    }

    @PostMapping("/")
    void connectNewHeater(@RequestBody HeaterRequest request) {
        heaterService.createHeater(request.getHeaterId(), request.getTemperature());
    }

    @PutMapping("/{heaterId}")
    void changeTemperatureSetting(@PathVariable Long heaterId, @RequestBody HeatureTemperatureRequest request) {
        heaterService.changeTemperatureSetting(heaterId, request.getTemperature());
    }
}
