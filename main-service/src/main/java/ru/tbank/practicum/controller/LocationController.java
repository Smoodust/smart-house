package ru.tbank.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.DetailedLocationDTO;
import ru.tbank.practicum.controller.dto.LocationCreationRequest;
import ru.tbank.practicum.controller.dto.LocationDTO;
import ru.tbank.practicum.controller.dto.LocationNameChangeRequest;
import ru.tbank.practicum.repository.entity.Location;
import ru.tbank.practicum.service.LocationService;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/locations")
public class LocationController {
  private final LocationService locationService;

  @GetMapping("")
  @Operation(summary = "Get all locations of that user", security = @SecurityRequirement(name = ""))
  List<LocationDTO> getAllLocations(@AuthenticationPrincipal UserDetails userDetails) {
    List<Location> locations = locationService.getAllLocations(userDetails);
    return locations.stream().map(LocationDTO::new).toList();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get detailed location info by its id",
      security = @SecurityRequirement(name = ""))
  DetailedLocationDTO getLocation(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    return new DetailedLocationDTO(locationService.getLocation(id, userDetails));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Change location name", security = @SecurityRequirement(name = ""))
  void changeLocationName(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody LocationNameChangeRequest request) {
    locationService.changeLocationName(id, request.getNewName(), userDetails);
  }

  @PostMapping("")
  @Operation(summary = "Create new location", security = @SecurityRequirement(name = ""))
  DetailedLocationDTO createNewLocation(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody LocationCreationRequest request) {
    return new DetailedLocationDTO(
        locationService.createNewLocation(request.getName(), userDetails.getUsername()));
  }
}
