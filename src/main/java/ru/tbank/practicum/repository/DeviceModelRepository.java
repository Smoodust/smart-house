package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.repository.entity.DeviceModel;

public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {}
