package ru.tbank.practicum.service;

import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.entity.User;

import javax.swing.text.html.Option;

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
        user.setRoles(Set.of("USER"));
        userRepository.save(user);
    }

    public Optional<User> getUserByUserDetail(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername());
    }
}
