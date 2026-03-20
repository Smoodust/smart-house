package ru.tbank.practicum.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.dto.DeviceTemp;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceTemp> getAllDevices() {
        return deviceRepository.getAllDevices();
    }

    public DeviceTemp getDeviceById(long id) {
        DeviceTemp deviceTemp = deviceRepository.getDevicebyId(id);
        if (deviceTemp == null) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        return deviceTemp;
    }

    public void updateDeviceState(long id, Map<String, Object> newValues) {
        deviceRepository.updateDeviceState(id, newValues);
    }
}
