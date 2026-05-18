package ru.tbank.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.DeviceSettingDTO;
import ru.tbank.practicum.repository.entity.DeviceModel;
import ru.tbank.practicum.repository.settings.SettingDefinition;
import ru.tbank.practicum.service.DeviceModelService;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/model")
public class ModelDeviceController {
  private final DeviceModelService deviceModelService;

  @GetMapping("/{id}")
  @Operation(
      summary = "Get detailed information of device model",
      security = @SecurityRequirement(name = ""))
  ArrayList<DeviceSettingDTO> getDevice(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    DeviceModel model = deviceModelService.getDeviceModelById(id);
    ArrayList<DeviceSettingDTO> settingsDefinition = new ArrayList<>();
    for (SettingDefinition def : model.getSettings()) {
      settingsDefinition.add(
          new DeviceSettingDTO(def.getName(), def.getSettingTypeName(), def.getDefaultValue()));
    }
    return settingsDefinition;
  }
}
