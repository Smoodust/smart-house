package ru.tbank.practicum.repository.settings;

public class BooleanDefinition extends SettingDefinition {
    public BooleanDefinition(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public void validate(Object value) throws IllegalArgumentException {
        if (!(value instanceof Boolean)) {
            throw new IllegalArgumentException("Boolean value required");
        }
    }
}
