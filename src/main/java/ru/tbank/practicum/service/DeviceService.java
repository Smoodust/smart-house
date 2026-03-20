package ru.tbank.practicum.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.getAllDevices();
    }

    public Device getDeviceById(long id) {
        Device device = deviceRepository.getDevicebyId(id);
        if (device == null) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        return device;
    }

    public void updateDeviceState(long id, Map<String, Object> newValues) {
        deviceRepository.updateDeviceState(id, newValues);
    }
}
