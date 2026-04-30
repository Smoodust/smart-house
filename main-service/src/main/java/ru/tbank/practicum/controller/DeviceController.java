package ru.tbank.practicum.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.DeviceDTO;
import ru.tbank.practicum.service.DeviceService;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/device")
public class DeviceController {
  private final DeviceService deviceService;

  @GetMapping("")
  List<DeviceDTO> getAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
    return deviceService.getAllDevices(userDetails).stream().map(DeviceDTO::new).toList();
  }

  @GetMapping("/{id}")
  DeviceDTO getDevice(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    return new DeviceDTO(deviceService.getDeviceById(id, userDetails));
  }

  @PatchMapping("/{id}")
  public void updateDeviceState(
      @PathVariable Long id,
      @RequestBody Map<String, Object> payload,
      @AuthenticationPrincipal UserDetails userDetails) {
    deviceService.updateDeviceState(id, payload, userDetails);
  }
}
