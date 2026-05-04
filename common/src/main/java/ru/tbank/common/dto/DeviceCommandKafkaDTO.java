package ru.tbank.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCommandKafkaDTO {
    private String login;
    private String deviceId;
    private Map<String, Object> data;
}
