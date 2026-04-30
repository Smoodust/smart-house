package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.repository.entity.HistoricalDeviceData;

public interface HistoricalDataRepository extends JpaRepository<HistoricalDeviceData, Long> {}
