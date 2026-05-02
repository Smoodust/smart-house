package ru.tbank.practicum.service.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataKafkaDTO {
  private String login;
  private String pass;
  private String deviceId;
  private Map<String, Object> data;
}
