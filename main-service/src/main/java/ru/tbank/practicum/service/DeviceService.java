package ru.tbank.practicum.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.common.dto.DeviceCommandKafkaDTO;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.HistoricalDeviceData;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.repository.settings.SettingDefinition;

@RequiredArgsConstructor
@Service
public class DeviceService {
  private final DeviceRepository deviceRepository;
  private final UserService userService;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public List<Device> getAllDevices(UserDetails userDetails) {
    Optional<User> user = userService.getUserByUserDetail(userDetails);
    if (user.isEmpty()) {
      return new ArrayList<>();
    }
    return deviceRepository.findAllByUserId(user.get().getId());
  }

  public List<Device> getAllDevicesByLocation(Long locationId, UserDetails userDetails) {
    return deviceRepository.findAllByLocationIdAndUserId(locationId, userDetails.getUsername());
  }

  public Device getDeviceById(long id, UserDetails userDetails) {
    Optional<User> user = userService.getUserByUserDetail(userDetails);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("Error with authorization!");
    }

    Optional<Device> device = deviceRepository.findById(id);
    if (device.isEmpty()) {
      throw new DeviceNotFoundException("Device not found with id " + id);
    }
    if (!Objects.equals(device.get().getLocation().getUser().getId(), user.get().getId())) {
      throw new IllegalArgumentException("Forbidden to access others device!");
    }
    return device.get();
  }

  @Transactional
  public void changeDeviceName(long id, String newName, UserDetails userDetails) {
    deviceRepository.updateDeviceName(id, newName, userDetails.getUsername());
  }

  public void updateDeviceState(Device device, Map<String, Object> newValues) {
    HistoricalDeviceData data = new HistoricalDeviceData();
    HashMap<String, Object> newData = new HashMap<>(device.getLastHistoricalData().getSettings());
    for (Map.Entry<String, Object> entry : newValues.entrySet()) {
      String name = entry.getKey();
      SettingDefinition current = device.getModel().getSetting(name);
      if (current != null) {
        newData.put(name, current.convertAndValidate(entry.getValue()));
      }
    }
    data.setSettings(newData);
    data.setRecordedAt(Instant.now());
    device.addNewData(data);
    deviceRepository.save(device);
  }

  public void updateDeviceState(long id, Map<String, Object> newValues, UserDetails userDetails) {
    Device device = getDeviceById(id, userDetails);
    updateDeviceState(device, newValues);
  }

  public void updateDeviceStateWithUpdate(
      long id, Map<String, Object> newValues, UserDetails userDetails) {
    Device device = getDeviceById(id, userDetails);

    HashMap<String, Object> newData = new HashMap<>();
    for (Map.Entry<String, Object> entry : newValues.entrySet()) {
      String name = entry.getKey();
      SettingDefinition current = device.getModel().getSetting(name);
      if (current != null) {
        newData.put(name, current.convertAndValidate(entry.getValue()));
      }
    }

    DeviceCommandKafkaDTO result = new DeviceCommandKafkaDTO();
    result.setLogin(userDetails.getUsername());
    result.setDeviceId(device.getExternalId());
    result.setData(newData);
    kafkaTemplate.send("hubs.command", result);
  }
}
