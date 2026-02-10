package com.aniqa.contact_mgt.service.impl;

import com.aniqa.contact_mgt.dto.UserRegistrationLoginRequest;
import com.aniqa.contact_mgt.dto.UserResponse;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.repository.UserRepository;
import com.aniqa.contact_mgt.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class) //to roll back on exceptions
@RequiredArgsConstructor //for dependency injection

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRegistrationLoginRequest request) {

        log.info("Registering new user");

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword_hash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id {}", savedUser.getId());

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    @Override
    public UserResponse login(UserRegistrationLoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword_hash())) {
            throw new RuntimeException("Invalid credentials");
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword_hash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword_hash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password changed for user {}", userId);
    }
}
