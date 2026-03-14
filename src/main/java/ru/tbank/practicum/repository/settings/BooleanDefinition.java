package ru.tbank.practicum.repository.settings;

public class BooleanDefinition extends SettingDefinition {
    public BooleanDefinition(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Object convertAndValidate(Object value) throws IllegalArgumentException {
        if (value instanceof Boolean) {
            return value;
        }
        throw new IllegalArgumentException("Boolean value required");
    }
}
