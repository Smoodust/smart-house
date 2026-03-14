package ru.tbank.practicum.repository;

import ru.tbank.practicum.repository.dot.Device;

import java.util.List;
import java.util.Map;

public interface DeviceRepository {
    Device getDevicebyId(Long id);
    List<Device> getAllDevices();
    void updateDeviceState(Long id, Map<String, Object> newValues);
}
