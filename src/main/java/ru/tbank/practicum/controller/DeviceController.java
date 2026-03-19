package ru.tbank.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.dto.DeviceDTO;

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
