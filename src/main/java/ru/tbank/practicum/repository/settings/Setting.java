package ru.tbank.practicum.repository.settings;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.tbank.practicum.exceptions.NoSuchSettingFound;
import ru.tbank.practicum.repository.dot.DeviceModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Setting {
    @JsonIgnore
    private final DeviceModel deviceModel;
    private final Map<String, Object> values = new HashMap<>();

    public Setting(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
        deviceModel.getSettings().forEach(x -> this.set(x.getName(), x.getDefaultValue()));
    }

    public Setting(DeviceModel deviceModel, Map<String, Object> values) {
        this(deviceModel);
        values.forEach(this::set);
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void set(String name, Object value) {
        SettingDefinition currentDefinition = deviceModel.getSetting(name);
        if (currentDefinition == null) {
            throw new NoSuchSettingFound("There is no such setting as "+name);
        }
        values.put(name, currentDefinition.convertAndValidate(value));
    }

    public void setMap(Map<String, Object> newValues) {
        newValues.forEach(this::set);
    }

    public Object get(String name) {
        return values.get(name);
    }

    @JsonAnyGetter
    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(values);
    }
}
