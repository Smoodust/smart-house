package ru.tbank.practicum.repository;

import java.util.List;
import java.util.Map;
import ru.tbank.practicum.repository.dto.DeviceTemp;

public interface DeviceRepository {
    DeviceTemp getDevicebyId(long id);

    List<DeviceTemp> getAllDevices();

    void updateDeviceState(long id, Map<String, Object> newValues);
}
