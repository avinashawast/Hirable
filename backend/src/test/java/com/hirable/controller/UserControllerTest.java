package com.hirable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirable.dto.CreateUserRequest;
import com.hirable.dto.UserDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
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

    private String adminToken;

    @BeforeEach
    public void setUp() throws Exception {
        userRepository.deleteAll();
        authenticationService.initializeDemoUsers();

        // Get admin token
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin@hirable.com\",\"password\":\"admin123\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        adminToken = objectMapper.readTree(loginResponse).get("token").asText();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testGetUserById() throws Exception {
        User admin = userRepository.findByUsername("admin@hirable.com").orElseThrow();

        mockMvc.perform(get("/api/users/" + admin.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin@hirable.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "newuser@email.com",
                "newuser@email.com",
                "password123",
                UserRole.JOB_SEEKER
        );

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser@email.com"))
                .andExpect(jsonPath("$.role").value("JOB_SEEKER"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    public void testCreateUserWithDuplicateUsername() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "admin@hirable.com",
                "duplicate@email.com",
                "password123",
                UserRole.JOB_SEEKER
        );

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User jobSeeker = userRepository.findByUsername("jobseeker@email.com").orElseThrow();

        UserDTO updateRequest = UserDTO.builder()
                .id(jobSeeker.getId())
                .username(jobSeeker.getUsername())
                .email("newemail@email.com")
                .role(UserRole.RECRUITER)
                .active(true)
                .build();

        mockMvc.perform(put("/api/users/" + jobSeeker.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newemail@email.com"))
                .andExpect(jsonPath("$.role").value("RECRUITER"));
    }

    @Test
    public void testDeactivateUser() throws Exception {
        User jobSeeker = userRepository.findByUsername("jobseeker@email.com").orElseThrow();

        mockMvc.perform(delete("/api/users/" + jobSeeker.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        User deactivatedUser = userRepository.findById(jobSeeker.getId()).orElseThrow();
        assert !deactivatedUser.isActive();
    }

    @Test
    public void testActivateUser() throws Exception {
        User jobSeeker = userRepository.findByUsername("jobseeker@email.com").orElseThrow();
        jobSeeker.setActive(false);
        userRepository.save(jobSeeker);

        mockMvc.perform(post("/api/users/" + jobSeeker.getId() + "/activate")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        User activatedUser = userRepository.findById(jobSeeker.getId()).orElseThrow();
        assert activatedUser.isActive();
    }

    @Test
    public void testGetUsersByRole() throws Exception {
        mockMvc.perform(get("/api/users/role/ADMIN")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    public void testGetUsersByStatus() throws Exception {
        mockMvc.perform(get("/api/users/status/true")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testUnauthorizedAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testForbiddenAccessForNonAdmin() throws Exception {
        // Get recruiter token
        String recruiterResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"recruiter@techcorp.com\",\"password\":\"recruiter123\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String recruiterToken = objectMapper.readTree(recruiterResponse).get("token").asText();

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isForbidden());
    }
}
