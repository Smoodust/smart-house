package ru.tbank.practicum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceChangeNameRequest {
  private String newName;
}
