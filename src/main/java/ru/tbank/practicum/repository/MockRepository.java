package ru.tbank.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.dot.Device;
import ru.tbank.practicum.repository.dot.DeviceModel;
import ru.tbank.practicum.repository.dot.Heater;
import ru.tbank.practicum.repository.settings.BooleanDefinition;

import java.util.*;

@Repository
public class MockRepository implements HeaterRepository, DeviceRepository {
    HashMap<Long, Heater> heaters = new HashMap<>();
    HashMap<Long, Device> devices = new HashMap<>();
    HashMap<Long, DeviceModel> deviceModels = new HashMap<>();

    public MockRepository() {
        deviceModels.put(0L, new DeviceModel(0L, "Heater", new ArrayList<>(List.of(new BooleanDefinition("turnOnOff", false)))));

        devices.put(0L, new Device(0L, "Kitchen Heater", deviceModels.get(0L)));
    }

    @Override
    public Heater getHeaterbyId(Long heaterId) {
        if (heaters.containsKey(heaterId)) {
            return heaters.get(heaterId);
        }
        return null;
    }

    @Override
    public void createNewHeater(Long heaterId, Long temperature) {
        if (heaters.containsKey(heaterId)) {
            return;
        }
        heaters.put(heaterId, new Heater(heaterId, temperature));
    }

    @Override
    public void changeTemperature(Long heaterId, Long temperature) {
        if (!heaters.containsKey(heaterId)) {
            return;
        }
        Heater temp = heaters.get(heaterId);
        temp.setTemperature(temperature);
        heaters.put(heaterId, temp);
    }

    @Override
    public Device getDevicebyId(Long id) {
        return devices.get(id);
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
