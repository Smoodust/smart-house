package ru.tbank.practicum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tbank.practicum.repository.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
  List<Location> findAllByUserLogin(String login);

  @Modifying
  @Query("UPDATE Location l SET l.name = :name WHERE l.locationId = :id AND l.user.login = :login")
  void updateLocationName(
      @Param("id") Long id, @Param("name") String name, @Param("login") String login);
}
