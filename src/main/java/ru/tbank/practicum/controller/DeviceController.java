package ru.tbank.practicum.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.repository.dto.DeviceDTO;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.service.DeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("")
    List<DeviceDTO> getDevices() {
        return deviceService.getAllDevices().stream().map(DeviceDTO::new).toList();
    }

    @GetMapping("/{id}")
    DeviceDTO getDevice(@PathVariable Long id) {
        return new DeviceDTO(deviceService.getDeviceById(id));
    }

    @PatchMapping("/{id}")
    public void updateDeviceState(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        deviceService.updateDeviceState(id, payload);
    }
}
