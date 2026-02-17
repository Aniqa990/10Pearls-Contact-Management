package com.aniqa.contact_mgt.service.impl;

import com.aniqa.contact_mgt.dto.UserRegistrationLoginRequest;
import com.aniqa.contact_mgt.dto.UserResponse;
import com.aniqa.contact_mgt.exception.EmailAlreadyExistsException;
import com.aniqa.contact_mgt.exception.InvalidCredentialsException;
import com.aniqa.contact_mgt.exception.ResourceNotFoundException;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.repository.UserRepository;
import com.aniqa.contact_mgt.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Unit Tests for UserServiceImpl

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRegistrationLoginRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setEmail("john@example.com");
        testUser.setPassword_hash("hashedPassword");
        testUser.setRole("USER");

        testRequest = new UserRegistrationLoginRequest();
        testRequest.setEmail("john@example.com");
        testRequest.setPassword("password123");
    }

    // REgistration tests

    
    @Test
    @DisplayName("Should register new user successfully")
    void testRegisterSuccess() {
        when(userRepository.existsByEmail(testRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateToken(testUser.getId())).thenReturn("jwtToken123");

        UserResponse response = userService.register(testRequest);

        assertNotNull(response);
        assertEquals("user123", response.getId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("jwtToken123", response.getToken());

        verify(userRepository, times(1)).save(any(User.class));
    }

    //Registration fails when email already exists test

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterEmailAlreadyExists() {
        when(userRepository.existsByEmail(testRequest.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.register(testRequest);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    // Login tests

    // successful login test

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void testLoginSuccess() {
        when(userRepository.findByEmail(testRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testRequest.getPassword(), testUser.getPassword_hash())).thenReturn(true);
        when(jwtTokenProvider.generateToken(testUser.getId())).thenReturn("jwtToken123");

        UserResponse response = userService.login(testRequest);

        assertNotNull(response);
        assertEquals("user123", response.getId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("jwtToken123", response.getToken());
    }

    //non-existent email test

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoginUserNotFound() {
        when(userRepository.findByEmail(testRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.login(testRequest);
        });
    }

    //Incorrect password test

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void testLoginIncorrectPassword() {

        when(userRepository.findByEmail(testRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testRequest.getPassword(), testUser.getPassword_hash())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.login(testRequest);
        });
    }

    // Change password test

    //password change with correct old password
    @Test
    @DisplayName("Should change password successfully")
    void testChangePasswordSuccess() {
        String userId = "user123";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(oldPassword, testUser.getPassword_hash())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.changePassword(userId, oldPassword, newPassword));

        verify(userRepository, times(1)).save(any(User.class));
    }

    //wrong old password test
    @Test
    @DisplayName("Should throw exception when old password is incorrect")
    void testChangePasswordWrongOldPassword() {
        String userId = "user123";
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.changePassword(userId, "wrongPassword", "newPassword");
        });
    }

    //changing but user not found
    @Test
    @DisplayName("Should throw exception when user not found during password change")
    void testChangePasswordUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.changePassword("nonExistentUser", "oldPass", "newPass");
        });
    }
}
