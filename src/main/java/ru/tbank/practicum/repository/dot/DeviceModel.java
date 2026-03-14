package ru.tbank.practicum.repository.dot;

import ru.tbank.practicum.repository.settings.SettingDefinition;

import java.util.List;

public class DeviceModel {
    private Long modelId;
    private String modelName;
    private List<SettingDefinition> settings;

    public Long getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public List<SettingDefinition> getSettings() {
        return settings;
    }

    public DeviceModel(Long modelId, String modelName, List<SettingDefinition> settings) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.settings = settings;
    }

    public SettingDefinition getSetting(String name) {
        return settings.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
