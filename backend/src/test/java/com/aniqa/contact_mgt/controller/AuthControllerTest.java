package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.dto.UserRegistrationRequest;
import com.aniqa.contact_mgt.dto.UserLoginRequest;
import com.aniqa.contact_mgt.dto.UserResponse;
import com.aniqa.contact_mgt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Integration Tests for AuthController

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserLoginRequest testLoginRequest;
    private UserRegistrationRequest testRegRequest;
    private UserResponse testResponse;

    @BeforeEach
    void setUp() {
        testRegRequest = new UserRegistrationRequest();
        testRegRequest.setEmail("john@example.com");
        testRegRequest.setPassword("password123");
        testRegRequest.setFirst_name("John");
        testRegRequest.setLast_name("Doe");

        testLoginRequest = new UserLoginRequest();
        testLoginRequest.setEmail("john@example.com");
        testLoginRequest.setPassword("password123");

        testResponse = UserResponse.builder()
                .id("user123")
                .email("john@example.com")
                .token("jwtToken123")
                .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterSuccess() throws Exception {
        when(userService.register(any(UserRegistrationRequest.class)))
                .thenReturn(testResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("user123"))
                .andExpect(jsonPath("$.data.token").value("jwtToken123"));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccess() throws Exception {
        when(userService.login(any(UserLoginRequest.class)))
                .thenReturn(testResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwtToken123"));
    }

    @Test
    @DisplayName("Should reject request with invalid email")
    void testRegisterInvalidEmail() throws Exception {
        testRegRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject request with null password")
    void testRegisterNullPassword() throws Exception {
        testRegRequest.setPassword(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegRequest)))
                .andExpect(status().isBadRequest());
    }
}
