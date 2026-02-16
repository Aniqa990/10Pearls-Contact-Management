package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.dto.UserLoginRequest;
import com.aniqa.contact_mgt.dto.UserRegistrationRequest;
import com.aniqa.contact_mgt.dto.UserResponse;
import com.aniqa.contact_mgt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
        public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        log.info("Register endpoint called for email: {}", request.getEmail());

        UserResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest request
    ) {
        log.info("Login endpoint called for email: {}", request.getEmail());

        UserResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}

