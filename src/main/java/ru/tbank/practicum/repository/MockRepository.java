package ru.tbank.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.dot.Device;
import ru.tbank.practicum.repository.dot.DeviceModel;
import ru.tbank.practicum.repository.settings.BooleanDefinition;
import ru.tbank.practicum.repository.settings.NumberDefinition;
import ru.tbank.practicum.repository.settings.StringDefinition;

import java.util.*;

@Repository
public class MockRepository implements DeviceRepository {
    HashMap<Long, Device> devices = new HashMap<>();
    HashMap<Long, DeviceModel> deviceModels = new HashMap<>();

    public MockRepository() {
        BooleanDefinition powerSetting = new BooleanDefinition("turnOnOff", false);
        NumberDefinition temperatureSetting = new NumberDefinition("temperature", 0);
        StringDefinition colorHueSetting = new StringDefinition("color", "#000000");
        NumberDefinition positionOfBlindsSetting = new NumberDefinition("positionBlinds", 0);
        deviceModels.put(0L, new DeviceModel(0L, "Heater", new ArrayList<>(List.of(powerSetting, temperatureSetting))));
        deviceModels.put(1L, new DeviceModel(1L, "Simple Lamp", new ArrayList<>(List.of(powerSetting))));
        deviceModels.put(2L, new DeviceModel(2L, "Dance Lamp", new ArrayList<>(List.of(powerSetting, colorHueSetting))));
        deviceModels.put(3L, new DeviceModel(3L, "Blinds", new ArrayList<>(List.of(powerSetting, positionOfBlindsSetting))));

        devices.put(0L, new Device(0L, "Kitchen Heater", deviceModels.get(0L)));
        devices.put(1L, new Device(1L, "Bedroom Heater", deviceModels.get(0L)));
        devices.put(2L, new Device(2L, "Kitchen Lamp", deviceModels.get(1L)));
        devices.put(3L, new Device(3L, "Bedroom Lamp", deviceModels.get(1L)));
        devices.put(4L, new Device(4L, "Hallway Lamp", deviceModels.get(1L)));
        devices.put(5L, new Device(5L, "Club Lamp", deviceModels.get(2L)));
        devices.put(6L, new Device(6L, "Bedroom Blinds", deviceModels.get(3L)));
    }

    @Override
    public Device getDevicebyId(Long id) {
        return devices.get(id);
    }

    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(devices.values());
    }

    @Override
    public void updateDeviceState(Long id, Map<String, Object> newValues) {
        Device currentDevice = devices.get(id);
        if (currentDevice == null) {
            return;
        }
        currentDevice.getSetting().setMap(newValues);
    }
}
