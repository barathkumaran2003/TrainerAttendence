package com.app.trainerattendence.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
