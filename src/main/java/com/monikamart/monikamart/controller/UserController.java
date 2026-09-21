package com.monikamart.monikamart.controller;

import com.monikamart.monikamart.dto.ApiResponse;
import com.monikamart.monikamart.dto.LoginRequest;
import com.monikamart.monikamart.dto.RegisterRequest;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            // Redact password in response
            registeredUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Registration successful! Welcome to MONIKA MART.", registeredUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request);
            user.setPassword(null); // Do not send password in response
            return ResponseEntity.ok(ApiResponse.success("Login successful!", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserProfile(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword(null);
            return ResponseEntity.ok(ApiResponse.success("User profile retrieved", u));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found with ID: " + id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUserProfile(@PathVariable Long id, @RequestBody User updatedData) {
        try {
            User updated = userService.updateProfile(id, updatedData);
            updated.setPassword(null);
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully!", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
