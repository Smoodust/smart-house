package ru.tbank.practicum.repository.settings;

public class NumberDefinition extends SettingDefinition
{
    public NumberDefinition(String name, Object defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Object convertAndValidate(Object value) throws IllegalArgumentException {
        if (value instanceof Number) {
            return value;
        }
        throw new IllegalArgumentException("Number value required");
    }
}
