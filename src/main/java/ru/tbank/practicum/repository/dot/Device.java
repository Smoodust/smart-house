package ru.tbank.practicum.repository.dot;

import ru.tbank.practicum.repository.settings.Setting;

import java.util.Set;

public class Device {
    private Long id;
    private String name;
    private DeviceModel model;
    private Setting setting;

    public Device(Long id, String name, DeviceModel model, Setting setting) {
        if (setting.getDeviceModel().getModelId() != model.getModelId()) {
            throw new IllegalArgumentException("Models ids for device and setting are different!");
        }

        this.id = id;
        this.name = name;
        this.model = model;
        this.setting = setting;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DeviceModel getModel() {
        return model;
    }

    public Setting getSetting() {
        return setting;
    }

    public Device(Long id, String name, DeviceModel model) {
        this(id, name, model, new Setting(model));
    }
}
