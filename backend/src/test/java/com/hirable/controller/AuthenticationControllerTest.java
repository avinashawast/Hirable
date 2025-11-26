package com.hirable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirable.dto.LoginRequest;
import com.hirable.entity.User;
import com.hirable.entity.UserRole;
import com.hirable.repository.UserRepository;
import com.hirable.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        authenticationService.initializeDemoUsers();
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin@hirable.com", "admin123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.username").value("admin@hirable.com"));
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin@hirable.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testLoginWithNonExistentUser() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent@email.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testLoginWithRecruiterCredentials() throws Exception {
        LoginRequest request = new LoginRequest("recruiter@techcorp.com", "recruiter123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("RECRUITER"));
    }

    @Test
    public void testLoginWithJobSeekerCredentials() throws Exception {
        LoginRequest request = new LoginRequest("jobseeker@email.com", "jobseeker123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("JOB_SEEKER"));
    }
}
