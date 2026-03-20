package ru.tbank.practicum.repository.settings;

public class StringDefinition extends SettingDefinition {
    public StringDefinition(String name, String defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Object convertAndValidate(Object value) throws IllegalArgumentException {
        if (value instanceof String) {
            return value;
        }
        throw new IllegalArgumentException("String value required");
    }
}
