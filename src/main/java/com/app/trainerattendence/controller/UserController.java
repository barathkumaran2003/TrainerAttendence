package com.app.trainerattendence.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.app.trainerattendence.model.User;
import com.app.trainerattendence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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
        return userService.getAllNormalUsers(); // ✅ Changed to filter only USER roles
    }

    @Operation(summary = "Get user by email", description = "Fetch user details by email address.")
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}
