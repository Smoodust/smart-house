package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long>  {
}
