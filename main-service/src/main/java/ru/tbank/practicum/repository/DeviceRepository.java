package ru.tbank.practicum.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.User;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
  Optional<Device> findById(Long id);

  @Query("SELECT d FROM Device d WHERE d.location.user.id = :userId")
  List<Device> findAllByUserId(@Param("userId") Long userId);

  Optional<Device> findByExternalIdAndLocationUser(String externalId, User user);
}
