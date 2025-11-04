package com.app.trainerattendence.service;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // ✅ New Login Logic
    public Object loginUser(String email, String password) {
        User existingUser = userRepository.findByEmail(email);

        Map<String, Object> response = new HashMap<>();

        if (existingUser == null) {
            response.put("status", "error");
            response.put("message", "User not found");
            return response;
        }

        if (!existingUser.getPassword().equals(password)) {
            response.put("status", "error");
            response.put("message", "Invalid password");
            return response;
        }

        // ✅ Successful login
        response.put("status", "success");
        response.put("message", "Login successful");
        response.put("user", existingUser);

        return response;
    }
}
