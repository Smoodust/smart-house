package ru.tbank.practicum.repository.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.tbank.practicum.repository.settings.SettingDefinition;

@Getter
@AllArgsConstructor
public class DeviceModel {
    private Long modelId;
    private String modelName;
    private List<SettingDefinition> settings;

    public SettingDefinition getSetting(String name) {
        return settings.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
