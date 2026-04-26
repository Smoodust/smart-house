package ru.tbank.practicum;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.ExpiredJwtException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.tbank.practicum.service.JwtService;

class JwtServiceTest {

  private JwtService jwtService;

  private final String SECRET = "mysecretkeymysecretkeymysecretkey12"; // минимум 32 байта
  private final long EXPIRATION = 1000 * 60 * 60; // 1 час

  private UserDetails userDetails;

  @BeforeEach
  void setUp() throws Exception {
    jwtService = new JwtService();

    setField(jwtService, "secret", SECRET);
    setField(jwtService, "expiration", EXPIRATION);

    userDetails = new User("testUser", "password", Collections.emptyList());
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JwtService.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @Test
  void shouldGenerateToken() {
    String token = jwtService.generateToken(userDetails);

    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Test
  void shouldExtractUsername() {
    String token = jwtService.generateToken(userDetails);

    String username = jwtService.extractUsername(token);

    assertEquals("testUser", username);
  }

  @Test
  void shouldExtractExpiration() {
    String token = jwtService.generateToken(userDetails);

    Date expiration = jwtService.extractExpiration(token);

    assertNotNull(expiration);
    assertTrue(expiration.after(new Date()));
  }

  @Test
  void shouldValidateCorrectToken() {
    String token = jwtService.generateToken(userDetails);

    Boolean isValid = jwtService.validateToken(token, userDetails);

    assertTrue(isValid);
  }

  @Test
  void shouldFailValidationForDifferentUser() {
    String token = jwtService.generateToken(userDetails);

    UserDetails anotherUser = new User("anotherUser", "password", Collections.emptyList());

    Boolean isValid = jwtService.validateToken(token, anotherUser);

    assertFalse(isValid);
  }

  @Test
  void shouldFailForExpiredToken() throws Exception {
    setField(jwtService, "expiration", 1L);
    String token = jwtService.generateToken(userDetails);
    Thread.sleep(5);
    assertThrows(
        ExpiredJwtException.class,
        () -> {
          jwtService.validateToken(token, userDetails);
        });
  }
}
