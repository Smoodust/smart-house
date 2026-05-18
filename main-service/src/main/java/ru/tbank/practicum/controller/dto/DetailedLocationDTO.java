package ru.tbank.practicum.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tbank.practicum.repository.entity.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailedLocationDTO {
  private Long id;
  private String name;
  private List<DeviceDTO> devices;

  public DetailedLocationDTO(Location location) {
    this.id = location.getLocationId();
    this.name = location.getName();
    this.devices = location.getDevices().stream().map(DeviceDTO::new).toList();
  }
}
