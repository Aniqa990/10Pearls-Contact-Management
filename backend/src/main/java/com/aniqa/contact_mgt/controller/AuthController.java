package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.dto.UserRegistrationLoginRequest;
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
            @Valid @RequestBody UserRegistrationLoginRequest request
    ) {
        log.info("API: Register user");

        UserResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserRegistrationLoginRequest request
    ) {
        log.info("API: Login user");

        UserResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
