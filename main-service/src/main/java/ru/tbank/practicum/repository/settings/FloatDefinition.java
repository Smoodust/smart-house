package ru.tbank.practicum.repository.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FloatDefinition extends SettingDefinition {
  public FloatDefinition(
      @JsonProperty("name") String name, @JsonProperty("defaultValue") Float defaultValue) {
    super(name, defaultValue);
  }

  @JsonIgnore
  @Override
  public String getSettingTypeName() {
    return "float";
  }

  @Override
  public Object convertAndValidate(Object value) throws IllegalArgumentException {
    if (value instanceof Number) {
      return ((Number) value).floatValue();
    }
    if (value instanceof String) {
      return Float.parseFloat((String) value);
    }
    throw new IllegalArgumentException("Number value required");
  }
}
