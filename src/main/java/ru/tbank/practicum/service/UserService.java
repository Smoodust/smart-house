package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
