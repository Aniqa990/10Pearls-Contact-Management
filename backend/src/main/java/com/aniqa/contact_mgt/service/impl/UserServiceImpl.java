package com.aniqa.contact_mgt.service.impl;


import com.aniqa.contact_mgt.dto.UserLoginRequest;
import com.aniqa.contact_mgt.dto.UserRegistrationRequest;
import com.aniqa.contact_mgt.dto.UserResponse;
import com.aniqa.contact_mgt.exception.EmailAlreadyExistsException;
import com.aniqa.contact_mgt.exception.InvalidCredentialsException;
import com.aniqa.contact_mgt.exception.ResourceNotFoundException;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.repository.UserRepository;
import com.aniqa.contact_mgt.security.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponse register(UserRegistrationRequest request) {

        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email {} already exists", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists. Please use a different email.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirst_name(request.getFirst_name());
        user.setLast_name(request.getLast_name());

        user.setPassword_hash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");  // default role for new users

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id {}", savedUser.getId());

        String token = jwtTokenProvider.generateToken(savedUser.getId());

        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirst_name())
                .lastName(savedUser.getLast_name())
                .token(token)
                .build();
    }


    @Override
    public UserResponse login(UserLoginRequest request) {
        
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found with email: {}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword_hash())) {
            log.warn("Login failed - invalid password for user: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        log.info("Login successful for user: {}", user.getId());

        // Generate JWT token valid for 1 hour
        String token = jwtTokenProvider.generateToken(user.getId());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .token(token)
                .build();
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {

        log.info("Change password request for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(oldPassword, user.getPassword_hash())) {
            log.warn("Change password failed - old password incorrect for user: {}", userId);
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        user.setPassword_hash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userId);
    }


    public UserResponse getUserProfile(String userId) {
        log.info("Fetching user profile for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .createdAt(user.getCreated_at())
                .build();
    }
}

