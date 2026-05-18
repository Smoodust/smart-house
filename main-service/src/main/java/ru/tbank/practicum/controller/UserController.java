package ru.tbank.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.AuthRequest;
import ru.tbank.practicum.controller.dto.UserDTO;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @PostMapping("")
  @Operation(summary = "Register new user")
  public void register(@Valid @RequestBody AuthRequest request) {
    userService.register(request.getLogin(), request.getPass());
  }

  @GetMapping("/me")
  @Operation(summary = "Get current user info", security = @SecurityRequirement(name = ""))
  public UserDTO getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
    Optional<User> user = userService.getUserByUserDetail(userDetails);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("Invalid credentials!");
    }
    return new UserDTO(user.get());
  }
}
