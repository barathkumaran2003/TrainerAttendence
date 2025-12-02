package com.app.trainerattendence.service;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    // ✅ Register New User
    @Override
    public User registerUser(User user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

    // ✅ Get Only Normal Users (Exclude Admins)
    @Override
    public List<User> getAllNormalUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .toList();

        List<User> reversed = new ArrayList<>();
        reversed.addAll(users);
        Collections.reverse(reversed);

        return reversed;
    }

    // ✅ Get All Users if needed internally
    @Override
    public List<User> getAllUsers() {
    	int a=0;
        List<User> all = userRepository.findAll();
        int size = all.size();
    	for(int i=size-1;i>=0;i--)
    	{
    		all.set(a++, all.get(i));
    	}
        return all;
    }

    // ✅ Get By Email
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Get By User ID
    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // ✅ Login Service
    @Override
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
    @Override
    public User uploadProfilePhoto(String userId, MultipartFile file) throws IOException {
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            return null;
        }

        user.setProfilePhoto(file.getBytes()); // Convert file → byte[]
        return userRepository.save(user);
    }

    // ✅ Get Profile Photo (Binary)
    @Override
    public byte[] getProfilePhoto(String userId) {
        User user = userRepository.findByUserId(userId);
        return (user != null) ? user.getProfilePhoto() : null;
    }
    
 // ------------------------------------------------------------
 // ✅ Change Password Service
 // ------------------------------------------------------------
 @Override
 public Object changePassword(String userId, String oldPassword, String newPassword) {

     Map<String, Object> response = new HashMap<>();

     User user = userRepository.findByUserId(userId);
     if (user == null) {
         response.put("status", "error");
         response.put("message", "User not found");
         return response;
     }

     if (!user.getPassword().equals(oldPassword)) {
         response.put("status", "error");
         response.put("message", "Old password is incorrect");
         return response;
     }

     user.setPassword(newPassword);
     userRepository.save(user);

     response.put("status", "success");
     response.put("message", "Password updated successfully");
     return response;
 }

 // ------------------------------------------------------------
 // ✅ Update Name Service
 // ------------------------------------------------------------
 @Override
 public Object updateName(String userId, String newName) {

     Map<String, Object> response = new HashMap<>();

     User user = userRepository.findByUserId(userId);
     if (user == null) {
         response.put("status", "error");
         response.put("message", "User not found");
         return response;
     }

     user.setName(newName);
     userRepository.save(user);

     response.put("status", "success");
     response.put("message", "Name updated successfully");
     response.put("updatedName", newName);
     return response;
 }

    
    
}
