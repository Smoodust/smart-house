package ru.tbank.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controller.dto.AuthRequest;
import ru.tbank.practicum.controller.dto.AuthResponse;
import ru.tbank.practicum.security.MyUserDetailsService;
import ru.tbank.practicum.service.JwtService;

@RequiredArgsConstructor
@RestController
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final MyUserDetailsService myUserDetailsService;
  private final JwtService jwtService;

  @PostMapping("/auth")
  @Operation(summary = "Authenticate and get JWT token", security = @SecurityRequirement(name = ""))
  public AuthResponse createAuthenticationToken(@Valid @RequestBody AuthRequest request)
      throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPass()));
    } catch (Exception e) {
      throw new Exception("Invalid credentials", e);
    }
    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getLogin());
    final String jwt = jwtService.generateToken(userDetails);

    return new AuthResponse(jwt);
  }
}
