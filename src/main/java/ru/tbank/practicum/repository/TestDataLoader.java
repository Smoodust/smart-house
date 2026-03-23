package ru.tbank.practicum.repository;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.DeviceModel;
import ru.tbank.practicum.repository.settings.BooleanDefinition;
import ru.tbank.practicum.repository.settings.FloatDefinition;
import ru.tbank.practicum.repository.settings.SettingDefinition;
import ru.tbank.practicum.repository.settings.StringDefinition;

@NoArgsConstructor
@Component
public class TestDataLoader {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @PostConstruct
    public void init() {
        deviceRepository.deleteAll();
        deviceModelRepository.deleteAll();

        BooleanDefinition powerSetting = new BooleanDefinition("turnOnOff", false);
        FloatDefinition temperatureSetting = new FloatDefinition("temperature", 0f);
        StringDefinition colorHueSetting = new StringDefinition("color", "#000000");
        FloatDefinition positionOfBlindsSetting = new FloatDefinition("positionBlinds", 0f);

        DeviceModel heater = createAndSaveDeviceModel("Heater", List.of(powerSetting, temperatureSetting));
        DeviceModel simpleLamp = createAndSaveDeviceModel("Simple Lamp", List.of(powerSetting));
        DeviceModel danceLamp = createAndSaveDeviceModel("Dance Lamp", List.of(powerSetting, colorHueSetting));
        DeviceModel blinds = createAndSaveDeviceModel("Blinds", List.of(powerSetting, positionOfBlindsSetting));

        createAndSaveDevice("Kitchen Heater", heater);
        createAndSaveDevice("Bedroom Heater", heater);
        createAndSaveDevice("Kitchen Lamp", simpleLamp);
        createAndSaveDevice("Bedroom Lamp", simpleLamp);
        createAndSaveDevice("Hallway Lamp", simpleLamp);
        createAndSaveDevice("Club Lamp", danceLamp);
        createAndSaveDevice("Bedroom Blind", blinds);
    }

    private DeviceModel createAndSaveDeviceModel(String modelName, List<SettingDefinition> definitions) {
        DeviceModel model = new DeviceModel();
        model.setModelName(modelName);
        model.setSettings(definitions);
        return deviceModelRepository.save(model);
    }

    private Device createAndSaveDevice(String name, DeviceModel model) {
        Device device = new Device();
        device.setName(name);
        device.setModel(model);
        Map<String, Object> updates = new HashMap<>();
        for (SettingDefinition definition : model.getSettings()) {
            updates.put(definition.getName(), definition.getDefaultValue());
        }
        device.updateSettings(updates);
        return deviceRepository.save(device);
    }
}
