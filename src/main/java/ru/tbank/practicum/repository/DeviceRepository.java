package ru.tbank.practicum.repository;

import ru.tbank.practicum.repository.dot.Device;

import java.util.Map;

public interface DeviceRepository {
    Device getDevicebyId(Long id);
    void updateDeviceState(Long id, Map<String, Object> newValues);
}
