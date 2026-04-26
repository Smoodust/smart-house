package ru.tbank.practicum.repository.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BooleanDefinition extends SettingDefinition {
  @JsonCreator
  public BooleanDefinition(
      @JsonProperty("name") String name, @JsonProperty("defaultValue") Boolean defaultValue) {
    super(name, defaultValue);
  }

  @Override
  public Object convertAndValidate(Object value) throws IllegalArgumentException {
    if (value instanceof Boolean) {
      return value;
    }
    if (value instanceof String) {
      String str = ((String) value).toLowerCase();
      if ("true".equals(str) || "false".equals(str)) {
        return Boolean.valueOf(str);
      }
    }
    throw new IllegalArgumentException("Boolean value required");
  }
}
