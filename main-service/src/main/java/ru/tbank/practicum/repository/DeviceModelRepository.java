package ru.tbank.practicum.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.repository.entity.DeviceModel;

public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {
  Optional<DeviceModel> findByModelId(Long modelId);
}
