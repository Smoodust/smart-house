package ru.tbank.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.DeviceChangeNameRequest;
import ru.tbank.practicum.controller.dto.DeviceDTO;
import ru.tbank.practicum.service.DeviceService;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class DeviceController {
  private final DeviceService deviceService;

  @GetMapping("/devices")
  @Operation(summary = "Get all devices by that user", security = @SecurityRequirement(name = ""))
  List<DeviceDTO> getAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
    return deviceService.getAllDevices(userDetails).stream().map(DeviceDTO::new).toList();
  }

  @GetMapping("/devices/{id}")
  @Operation(summary = "Get concrete device info", security = @SecurityRequirement(name = ""))
  DeviceDTO getDevice(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    return new DeviceDTO(deviceService.getDeviceById(id, userDetails));
  }

  @PatchMapping("/devices/{id}")
  @Operation(summary = "Update state of device", security = @SecurityRequirement(name = ""))
  public void updateDeviceState(
      @PathVariable Long id,
      @RequestBody Map<String, Object> payload,
      @AuthenticationPrincipal UserDetails userDetails) {
    deviceService.updateDeviceStateWithUpdate(id, payload, userDetails);
  }

  @PatchMapping("/devices/{id}/name")
  @Operation(summary = "Change name of device", security = @SecurityRequirement(name = ""))
  public void updateDeviceState(
      @PathVariable Long id,
      @RequestBody DeviceChangeNameRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    deviceService.changeDeviceName(id, request.getNewName(), userDetails);
  }

  @GetMapping("/locations/{location_id}/devices")
  @Operation(summary = "Get devices by location id", security = @SecurityRequirement(name = ""))
  public List<DeviceDTO> getDevicesInLocation(
      @PathVariable Long location_id, @AuthenticationPrincipal UserDetails userDetails) {
    return deviceService.getAllDevicesByLocation(location_id, userDetails).stream()
        .map(DeviceDTO::new)
        .toList();
  }
}
