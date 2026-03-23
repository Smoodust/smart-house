package ru.tbank.practicum.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    public Optional<Device> findById(Long id);
}
