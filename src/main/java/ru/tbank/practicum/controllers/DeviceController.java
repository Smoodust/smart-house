package ru.tbank.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.exceptions.HeaterNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.dot.Device;
import ru.tbank.practicum.repository.dot.DeviceDTO;

import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/{id}")
    DeviceDTO getDevice(@PathVariable Long id) {
        Device device = deviceRepository.getDevicebyId(id);
        if (device == null) {
            throw new HeaterNotFoundException("Device not found with id " + id);
        }
        return new DeviceDTO(device);
    }

    @PatchMapping("/{id}")
    public void updateDeviceState(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        deviceRepository.updateDeviceState(id, payload);
    }
}
