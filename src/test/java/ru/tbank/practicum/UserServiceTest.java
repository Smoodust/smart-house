package ru.tbank.practicum;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetAnUser() {
        String login = "login";
        String password = "password";

        User existingUser = new User();
        existingUser.setLogin(login);
        existingUser.setPassHash(password);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return login;
            }
        };

        Optional<User> user = userService.getUserByUserDetail(userDetails);
        assertEquals(true, user.isPresent());
        assertEquals(existingUser, user.get());
    }

    @Test
    void shouldRegisterNewUser() {
        String login = "login";
        String password = "password";
        String encodedPassword = "encodedPass";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        userService.register(login, password);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(login, savedUser.getLogin());
        assertEquals(encodedPassword, savedUser.getPassHash());
        assertTrue(savedUser.getRoles().contains("USER"));
    }

    @Test
    void shouldRegisterThrowAnException() {
        String login = "login";
        String password = "password";

        User existingUser = new User();
        existingUser.setLogin(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(login, password);
        });
    }
}
