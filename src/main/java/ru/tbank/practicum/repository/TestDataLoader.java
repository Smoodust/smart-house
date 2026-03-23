package ru.tbank.practicum.repository;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.DeviceModel;
import ru.tbank.practicum.repository.entity.Location;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.repository.settings.BooleanDefinition;
import ru.tbank.practicum.repository.settings.FloatDefinition;
import ru.tbank.practicum.repository.settings.SettingDefinition;
import ru.tbank.practicum.repository.settings.StringDefinition;

@NoArgsConstructor
@Component
public class TestDataLoader {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        userRepository.deleteAll();
        locationRepository.deleteAll();
        deviceRepository.deleteAll();
        deviceModelRepository.deleteAll();

        User user1 = createAndSaveUser("test", "test");
        User user2 = createAndSaveUser("pupa", "lupa");

        Location loc1 = createAndSaveLocation("Test Kitchen", user1);
        Location loc2 = createAndSaveLocation("Test Bedroom", user1);
        Location loc3 = createAndSaveLocation("Test Hallway", user2);
        Location loc4 = createAndSaveLocation("Test Club", user2);
        Location loc5 = createAndSaveLocation("Test Bedroom", user2);

        BooleanDefinition powerSetting = new BooleanDefinition("turnOnOff", false);
        FloatDefinition temperatureSetting = new FloatDefinition("temperature", 0f);
        StringDefinition colorHueSetting = new StringDefinition("color", "#000000");
        FloatDefinition positionOfBlindsSetting = new FloatDefinition("positionBlinds", 0f);

        DeviceModel heater = createAndSaveDeviceModel("Heater", List.of(powerSetting, temperatureSetting));
        DeviceModel simpleLamp = createAndSaveDeviceModel("Simple Lamp", List.of(powerSetting));
        DeviceModel danceLamp = createAndSaveDeviceModel("Dance Lamp", List.of(powerSetting, colorHueSetting));
        DeviceModel blinds = createAndSaveDeviceModel("Blinds", List.of(powerSetting, positionOfBlindsSetting));

        createAndSaveDevice("Kitchen Heater", heater, loc1);
        createAndSaveDevice("Bedroom Heater", heater, loc2);
        createAndSaveDevice("Kitchen Lamp", simpleLamp, loc1);
        createAndSaveDevice("Bedroom Lamp", simpleLamp, loc2);
        createAndSaveDevice("Hallway Lamp", simpleLamp, loc3);
        createAndSaveDevice("Club Lamp", danceLamp, loc4);
        createAndSaveDevice("Bedroom Blind", blinds, loc5);
    }

    private User createAndSaveUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private Location createAndSaveLocation(String name, User user) {
        Location location = new Location();
        location.setName(name);
        location.setUser(user);
        return locationRepository.save(location);
    }

    private DeviceModel createAndSaveDeviceModel(String modelName, List<SettingDefinition> definitions) {
        DeviceModel model = new DeviceModel();
        model.setModelName(modelName);
        model.setSettings(definitions);
        return deviceModelRepository.save(model);
    }

    private Device createAndSaveDevice(String name, DeviceModel model, Location location) {
        Device device = new Device();
        device.setName(name);
        device.setModel(model);
        device.setLocation(location);
        Map<String, Object> updates = new HashMap<>();
        for (SettingDefinition definition : model.getSettings()) {
            updates.put(definition.getName(), definition.getDefaultValue());
        }
        device.updateSettings(updates);
        return deviceRepository.save(device);
    }
}
