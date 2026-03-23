package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.tbank.practicum.controller.dto.AuthRequest;
import ru.tbank.practicum.controller.dto.AuthResponse;
import ru.tbank.practicum.service.CustomUserDetailsService;
import ru.tbank.practicum.utils.JwtUtil;

@Controller
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth")
    public AuthResponse createAuthenticationToken(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPass()));
        UserDetails details = userDetailsService.loadUserByUsername(request.getLogin());
        String jwt = jwtUtil.generateToken(details);
        return new AuthResponse(jwt);
    }
}
