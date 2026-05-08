package ru.tbank.practicum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.service.dto.DeviceDataKafkaDTO;

@Slf4j
@Component
public class KafkaDataConsumer {
  private final ObjectMapper mapper = new ObjectMapper();
  private final UserService userService;
  private final DeviceRepository deviceRepository;
  private final DeviceService deviceService;

  public KafkaDataConsumer(
      UserService userService, DeviceRepository deviceRepository, DeviceService deviceService) {
    this.userService = userService;
    this.deviceRepository = deviceRepository;
    this.deviceService = deviceService;
  }

  @KafkaListener(topics = "hubs.data", groupId = "main-service")
  @Transactional
  public void listen(String message) {
    log.debug("Received Kafka message: {}", message);

    DeviceDataKafkaDTO parsedMessage;
    try {
      parsedMessage = mapper.readValue(message, DeviceDataKafkaDTO.class);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse Kafka message: {}", e.getMessage(), e);
      return;
    }

    User user =
        userService.getUserByLoginAndPass(parsedMessage.getLogin(), parsedMessage.getPass());

    Optional<Device> device =
        deviceRepository.findByExternalIdAndLocationUser(parsedMessage.getDeviceId(), user);

    if (device.isEmpty()) {
      log.warn(
          "Device not found for externalId={} and user={}",
          parsedMessage.getDeviceId(),
          user != null ? user.getLogin() : "unknown");
      // TODO: запрос на discovery нового устройства
      return;
    }

    deviceService.updateDeviceState(device.get(), parsedMessage.getData());
    log.info("Successfully updated state for device externalId={}", parsedMessage.getDeviceId());
  }
}
