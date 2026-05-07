package ru.tbank.practicum.repository.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StringDefinition extends SettingDefinition {
  public StringDefinition(
      @JsonProperty("name") String name, @JsonProperty("defaultValue") String defaultValue) {
    super(name, defaultValue);
  }

  @JsonIgnore
  @Override
  public String getSettingTypeName() {
    return "string";
  }

  @Override
  public Object convertAndValidate(Object value) throws IllegalArgumentException {
    if (value instanceof String) {
      return value;
    }
    throw new IllegalArgumentException("String value required");
  }
}
