package ru.tbank.practicum.repository.dto;

import java.util.Objects;
import lombok.Getter;
import ru.tbank.practicum.repository.settings.Setting;

@Getter
public class DeviceTemp {
    private Long id;
    private String name;
    private DeviceModel model;
    private Setting setting;

    public DeviceTemp(Long id, String name, DeviceModel model, Setting setting) {
        if (!Objects.equals(setting.getDeviceModel().getModelId(), model.getModelId())) {
            throw new IllegalArgumentException("Models ids for device and setting are different!");
        }

        this.id = id;
        this.name = name;
        this.model = model;
        this.setting = setting;
    }

    public DeviceTemp(Long id, String name, DeviceModel model) {
        this(id, name, model, new Setting(model));
    }
}
