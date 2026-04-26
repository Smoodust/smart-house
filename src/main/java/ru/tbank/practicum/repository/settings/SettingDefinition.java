package ru.tbank.practicum.repository.settings;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = BooleanDefinition.class, name = "boolean"),
  @JsonSubTypes.Type(value = FloatDefinition.class, name = "number"),
  @JsonSubTypes.Type(value = StringDefinition.class, name = "string")
})
public abstract class SettingDefinition {
  private final String name;
  private final Object defaultValue;

  public SettingDefinition(String name, Object defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  public abstract Object convertAndValidate(Object value) throws IllegalArgumentException;
}
