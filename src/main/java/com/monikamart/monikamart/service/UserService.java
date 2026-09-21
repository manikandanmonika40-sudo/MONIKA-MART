package com.monikamart.monikamart.service;

import com.monikamart.monikamart.dto.LoginRequest;
import com.monikamart.monikamart.dto.RegisterRequest;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new RuntimeException("Email is already registered. Please login instead.");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword()); // In production hash, keep simple for project requirement
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPincode(request.getPincode());
        user.setRole(request.getRole() != null && !request.getRole().isBlank() ? request.getRole().toUpperCase() : "USER");

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with email " + email + " does not exist.");
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password. Please try again.");
        }

        return user;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateProfile(Long userId, User updatedData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (updatedData.getFullName() != null && !updatedData.getFullName().isBlank()) {
            user.setFullName(updatedData.getFullName().trim());
        }
        if (updatedData.getPhone() != null) {
            user.setPhone(updatedData.getPhone().trim());
        }
        if (updatedData.getAddress() != null) {
            user.setAddress(updatedData.getAddress().trim());
        }
        if (updatedData.getCity() != null) {
            user.setCity(updatedData.getCity().trim());
        }
        if (updatedData.getState() != null) {
            user.setState(updatedData.getState().trim());
        }
        if (updatedData.getPincode() != null) {
            user.setPincode(updatedData.getPincode().trim());
        }
        if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
            user.setPassword(updatedData.getPassword());
        }

        return userRepository.save(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
