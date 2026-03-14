package ru.tbank.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.exceptions.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.dot.Device;
import ru.tbank.practicum.repository.dot.DeviceDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("")
    List<DeviceDTO> getDevices() {
        return deviceRepository.getAllDevices().stream().map(DeviceDTO::new).toList();
    }

    @GetMapping("/{id}")
    DeviceDTO getDevice(@PathVariable Long id) {
        Device device = deviceRepository.getDevicebyId(id);
        if (device == null) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        return new DeviceDTO(device);
    }

    @PatchMapping("/{id}")
    public void updateDeviceState(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        deviceRepository.updateDeviceState(id, payload);
    }
}
