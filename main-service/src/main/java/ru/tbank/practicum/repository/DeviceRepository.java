package ru.tbank.practicum.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.Location;
import ru.tbank.practicum.repository.entity.User;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
  Optional<Device> findById(Long id);

  @Query("SELECT d FROM Device d WHERE d.location.user.id = :userId")
  List<Device> findAllByUserId(@Param("userId") Long userId);

  @Query(
      "SELECT d FROM Device d WHERE d.location.locationId = :locationId AND d.location.user.login = :login")
  List<Device> findAllByLocationIdAndUserId(
      @Param("locationId") Long locationId, @Param("login") String login);

  Optional<Device> findByExternalIdAndLocationUser(String externalId, User user);

  @Modifying
  @Query("UPDATE Device d SET d.location = null WHERE d.location.locationId = :locationId")
  void detachDevicesFromLocation(@Param("locationId") Long locationId);

  // 5. Change device name by id
  @Modifying
  @Query("UPDATE Device d SET d.name = :name WHERE d.id = :id AND d.location.user.login = :login")
  void updateDeviceName(
      @Param("id") Long id, @Param("name") String name, @Param("login") String login);

  // 6. Assign/reassign device to a location (requires the Location entity to be passed)
  @Modifying
  @Query("UPDATE Device d SET d.location = :location WHERE d.id = :deviceId")
  void assignDeviceToLocation(
      @Param("deviceId") Long deviceId, @Param("location") Location location);
}
