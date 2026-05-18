package ru.tbank.practicum.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.repository.LocationRepository;
import ru.tbank.practicum.repository.entity.Location;
import ru.tbank.practicum.repository.entity.User;

@RequiredArgsConstructor
@Service
public class LocationService {
  private final LocationRepository locationRepository;
  private final UserService userService;

  public List<Location> getAllLocations(UserDetails userDetails) {
    return locationRepository.findAllByUserLogin(userDetails.getUsername());
  }

  public Location getLocation(Long id, UserDetails userDetails) {
    Optional<Location> location = locationRepository.findById(id);
    if (location.isEmpty()) {
      throw new IllegalArgumentException("There is no such location!");
    }
    if (!Objects.equals(location.get().getUser().getLogin(), userDetails.getUsername())) {
      throw new IllegalArgumentException("There is no such location!");
    }
    return location.get();
  }

  @Transactional
  public void changeLocationName(Long id, String newName, UserDetails userDetails) {
    locationRepository.updateLocationName(id, newName, userDetails.getUsername());
  }

  @Transactional
  public Location createNewLocation(String name, String login) {
    Optional<User> user = Optional.ofNullable(userService.getUserByLogin(login));
    if (user.isEmpty()) {
      throw new IllegalArgumentException("Illegal credentials!");
    }

    Location location = new Location();
    location.setName(name);
    location.setUser(user.get());
    return locationRepository.save(location);
  }
}
