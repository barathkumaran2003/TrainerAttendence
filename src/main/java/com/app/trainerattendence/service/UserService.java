package com.app.trainerattendence.service;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

    // ✅ Modified to return only "USER" roles
    public List<User> getAllNormalUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}
