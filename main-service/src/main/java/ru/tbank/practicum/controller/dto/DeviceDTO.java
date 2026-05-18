package ru.tbank.practicum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.settings.Setting;

@Getter
@Setter
@AllArgsConstructor
public class DeviceDTO {
  private Long id;
  private String externalId;
  private Long idModel;
  private Long idLocation;
  private String name;
  private Setting setting;

  public DeviceDTO(Device device) {
    this.id = device.getId();
    this.idLocation = device.getLocation().getLocationId();
    this.externalId = device.getExternalId();
    this.idModel = device.getModel().getModelId();
    this.name = device.getName();
    this.setting = new Setting(device.getModel(), device.getLastHistoricalData().getSettings());
  }
}
