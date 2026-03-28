package ru.tbank.practicum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.HistoricalDataRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.HistoricalDeviceData;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.repository.settings.SettingDefinition;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    public List<Device> getAllDevices(UserDetails userDetails) {
        Optional<User> user = userService.getUserByUserDetail(userDetails);
        if (user.isEmpty()) {
            return new ArrayList<>();
        }
        return deviceRepository.findAllByUserId(user.get().getId());
    }

    public Device getDeviceById(long id, UserDetails userDetails) {
        Optional<User> user = userService.getUserByUserDetail(userDetails);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Error with authorization!");
        }

        Optional<Device> device = deviceRepository.findById(id);
        if (device.isEmpty()) {
            throw new DeviceNotFoundException("Device not found with id " + id);
        }
        if (!Objects.equals(device.get().getLocation().getUser().getId(), user.get().getId())) {
            throw new IllegalArgumentException("Forbidden to access others device!");
        }
        return device.get();
    }

    public void updateDeviceState(long id, Map<String, Object> newValues, UserDetails userDetails) {
        Device device = getDeviceById(id, userDetails);

        HistoricalDeviceData data = new HistoricalDeviceData();
        HashMap<String, Object> newData =
                new HashMap<>(device.getLastHistoricalData().getSettings());
        for (Map.Entry<String, Object> entry : newValues.entrySet()) {
            String name = entry.getKey();
            SettingDefinition current = device.getModel().getSetting(name);
            if (current != null) {
                newData.put(name, current.convertAndValidate(entry.getValue()));
            }
        }
        data.setSettings(newData);
        device.addNewData(data);
        deviceRepository.save(device);
    }
}
