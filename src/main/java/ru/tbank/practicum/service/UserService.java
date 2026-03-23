package ru.tbank.practicum.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.entity.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(@NonNull String login, @NonNull String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Account with such login already exist!");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
