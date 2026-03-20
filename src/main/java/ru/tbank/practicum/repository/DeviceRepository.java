package ru.tbank.practicum.repository;

import java.util.List;
import java.util.Map;
import ru.tbank.practicum.repository.entity.Device;

public interface DeviceRepository {
    Device getDevicebyId(long id);

    List<Device> getAllDevices();

    void updateDeviceState(long id, Map<String, Object> newValues);
}
