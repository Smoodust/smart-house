package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.repository.UserRepository;
import ru.tbank.practicum.repository.dto.UserDTO;
import ru.tbank.practicum.repository.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(x -> new UserDTO(x.getLogin(), x.getPass_hash()))
                .toList();
    }

    @PostMapping("")
    public User addUser(@RequestBody @Valid UserDTO newUser) {
        if (userRepository.findByLogin(newUser.login()).isPresent()) {
            throw new IllegalArgumentException("There is already such account!");
        }

        User user = new User();
        user.setLogin(newUser.login());
        user.setPass_hash(newUser.pass_hash());
        return userRepository.save(user);
    }
}
