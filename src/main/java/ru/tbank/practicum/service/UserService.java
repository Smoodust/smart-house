package ru.tbank.practicum.service;

import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.entity.User;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(@NonNull String login, @NonNull String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Account with such login already exist!");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassHash(passwordEncoder.encode(password));
        user.setRoles(Set.of("USER"));
        userRepository.save(user);
    }

    public Optional<User> getUserByUserDetail(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername());
    }
}
