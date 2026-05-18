package ru.tbank.practicum.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.repository.DeviceModelRepository;
import ru.tbank.practicum.repository.entity.DeviceModel;

@RequiredArgsConstructor
@Service
public class DeviceModelService {
  private final DeviceModelRepository deviceModelRepository;

  public DeviceModel getDeviceModelById(long id) {
    Optional<DeviceModel> model = deviceModelRepository.findByModelId(id);
    if (model.isEmpty()) {
      throw new DeviceNotFoundException("Device not found with id " + id);
    }
    return model.get();
  }
}
