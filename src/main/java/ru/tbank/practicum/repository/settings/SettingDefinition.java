package ru.tbank.practicum.repository.settings;

public abstract class SettingDefinition {
    private final String name;
    private final Object defaultValue;

    public SettingDefinition(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public SettingDefinition(String name) {
        this(name, null);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public abstract void validate(Object value) throws IllegalArgumentException;
}
