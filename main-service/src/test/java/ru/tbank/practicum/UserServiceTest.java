package ru.tbank.practicum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService userService;

  @Test
  void registerShouldSaveNewUser() {
    String login = "john";
    String rawPassword = "secret";
    String encodedPassword = "encodedSecret";

    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    userService.register(login, rawPassword);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals(login, savedUser.getLogin());
    assertEquals(encodedPassword, savedUser.getPassHash());
    assertEquals(Set.of("USER"), savedUser.getRoles());
  }

  @Test
  void registerShouldThrowWhenLoginAlreadyExists() {
    String login = "existing";
    String password = "irrelevant";

    User existingUser = new User();
    existingUser.setLogin(login);
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> userService.register(login, password));
    assertEquals("Account with such login already exist!", exception.getMessage());
    verify(userRepository, never()).save(any());
  }

  @Test
  void registerShouldThrowWhenLoginIsNull() {
    assertThrows(NullPointerException.class, () -> userService.register(null, "password"));
    verifyNoInteractions(userRepository);
  }

  @Test
  void registerShouldThrowWhenPasswordIsNull() {
    assertThrows(NullPointerException.class, () -> userService.register("login", null));
    verifyNoInteractions(userRepository);
  }

  @Test
  void getUserByLoginShouldReturnUserWhenFound() {
    String login = "john";
    User user = new User();
    user.setLogin(login);
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

    User result = userService.getUserByLogin(login);

    assertSame(user, result);
  }

  @Test
  void getUserByLoginShouldThrowWhenNotFound() {
    String login = "unknown";
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByLogin(login));
    assertEquals("There is no such user!", exception.getMessage());
  }

  @Test
  void getUserByLoginAndPassShouldReturnUserWhenCredentialsMatch() {
    String login = "john";
    String rawPassword = "secret";
    User user = new User();
    user.setLogin(login);
    user.setPassHash("storedHash");

    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, user.getPassHash())).thenReturn(true);

    User result = userService.getUserByLoginAndPass(login, rawPassword);

    assertSame(user, result);
  }

  @Test
  void getUserByLoginAndPassShouldThrowWhenUserNotFound() {
    String login = "ghost";
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class, () -> userService.getUserByLoginAndPass(login, "any"));
  }

  @Test
  void getUserByLoginAndPassShouldThrowWhenPasswordMismatch() {
    String login = "john";
    String rawPassword = "wrongPass";
    User user = new User();
    user.setLogin(login);
    user.setPassHash("storedHash");

    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, user.getPassHash())).thenReturn(false);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> userService.getUserByLoginAndPass(login, rawPassword));
    assertEquals("Password don't match!", exception.getMessage());
  }

  @Test
  void getUserByUserDetailShouldReturnUserWhenFound() {
    String login = "john";
    User user = new User();
    user.setLogin(login);

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(login);
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

    Optional<User> result = userService.getUserByUserDetail(userDetails);

    assertTrue(result.isPresent());
    assertSame(user, result.get());
  }

  @Test
  void getUserByUserDetailShouldReturnEmptyWhenNotFound() {
    String login = "unknown";
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(login);
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    Optional<User> result = userService.getUserByUserDetail(userDetails);

    assertTrue(result.isEmpty());
  }
}
