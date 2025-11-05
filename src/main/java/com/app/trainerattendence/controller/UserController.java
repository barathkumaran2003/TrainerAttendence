package com.app.trainerattendence.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "User APIs", description = "Endpoints for managing user accounts")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user with role (Admin/User).")
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @Operation(summary = "Get all users", description = "Returns only users with role 'USER' (excludes admins).")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllNormalUsers();
    }

    @Operation(summary = "Get user by email", description = "Fetch user details by email address.")
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // ✅ New Login API
    @Operation(summary = "User Login", description = "Logs in a user by verifying email and password.")
    @PostMapping("/login")
    public Object loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userService.loginUser(email, password);
    }

    // ------------------------------------------------------------
    // ✅ ✅ ✅ Added Code Below (Nothing above is changed)
    // ------------------------------------------------------------

    @Operation(summary = "Upload Profile Photo", description = "Upload user profile picture as binary data")
    @PostMapping(value = "/upload-photo/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User uploadProfilePhoto(
            @PathVariable String userId,
            @RequestPart("photo") MultipartFile photo
    ) throws Exception {
        return userService.uploadProfilePhoto(userId, photo);
    }

    @Operation(summary = "Get User Profile Photo", description = "Fetch user profile photo in binary image format")
    @GetMapping(value = "/photo/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProfilePhoto(@PathVariable String userId) {
        return userService.getProfilePhoto(userId);
    }

}
