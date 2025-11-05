package com.app.trainerattendence.service;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ Register New User
    public User registerUser(User user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

    // ✅ Get Only Normal Users (Exclude Admins)
    public List<User> getAllNormalUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .toList();
    }

    // ✅ Get All Users if needed internally
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get By Email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Get By User ID
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // ✅ Login Service
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

        response.put("status", "success");
        response.put("message", "Login successful");
        response.put("user", existingUser);

        return response;
    }

    // ✅ Upload Profile Photo (Binary)
    public User uploadProfilePhoto(String userId, MultipartFile file) throws IOException {
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            return null;
        }

        user.setProfilePhoto(file.getBytes()); // Convert file → byte[]
        return userRepository.save(user);
    }

    // ✅ Get Profile Photo (Binary)
    public byte[] getProfilePhoto(String userId) {
        User user = userRepository.findByUserId(userId);
        return (user != null) ? user.getProfilePhoto() : null;
    }
}
