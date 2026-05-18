package ru.tbank.practicum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tbank.practicum.repository.entity.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
  private Long id;
  private String name;

  public LocationDTO(Location location) {
    this.id = location.getLocationId();
    this.name = location.getName();
  }
}
