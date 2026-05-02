package ru.tbank.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.AclRequest;
import ru.tbank.practicum.controller.dto.MQTTAuthRequest;
import ru.tbank.practicum.controller.dto.MQTTAuthResponse;
import ru.tbank.practicum.service.MQTTAuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mqtt")
public class MQTTAuthController {
  private final MQTTAuthService mqttAuthService;

  @PostMapping("/auth")
  public MQTTAuthResponse authenticate(@RequestBody MQTTAuthRequest request) {
    return new MQTTAuthResponse(
        mqttAuthService.isAuthorized(request.getUsername(), request.getPassword()), "");
  }

  @PostMapping("/acl")
  public MQTTAuthResponse acl(@RequestBody AclRequest request) {
    return new MQTTAuthResponse(
        mqttAuthService.isAuthorizedAcl(
            request.getUsername(), request.getTopic(), request.getAcc() == 2),
        "");
  }
}
