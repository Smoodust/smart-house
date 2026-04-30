package ru.tbank.practicum.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;

public interface WeatherRepository extends JpaRepository<WeatherLocation, Long> {
  Optional<WeatherLocation> findByLatitudeAndLongtitude(Double latitude, Double longtitude);
}
