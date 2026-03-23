package ru.tbank.practicum.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceModelRepository;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;

@Service
public class DeviceService {
    /*@Autowired
    private DeviceRepositoryTemp deviceRepositoryTemp;

    public List<DeviceTemp> getAllDevices() {
        return deviceRepositoryTemp.getAllDevices();
    }

    public DeviceTemp getDeviceById(long id) {
        DeviceTemp deviceTemp = deviceRepositoryTemp.getDevicebyId(id);
        if (deviceTemp == null) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        return deviceTemp;
    }

    public void updateDeviceState(long id, Map<String, Object> newValues) {
        deviceRepositoryTemp.updateDeviceState(id, newValues);
    }*/
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(long id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isEmpty()) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        return device.get();
    }

    public void updateDeviceState(long id, Map<String, Object> newValues) {
        Device device = getDeviceById(id);
        device.updateSettings(newValues);
        deviceRepository.save(device);
    }
}
