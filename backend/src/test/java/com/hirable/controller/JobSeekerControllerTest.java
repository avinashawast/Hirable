package com.hirable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirable.dto.JobSeekerProfileDTO;
import com.hirable.entity.JobSeeker;
import com.hirable.entity.User;
import com.hirable.entity.UserRole;
import com.hirable.repository.JobSeekerRepository;
import com.hirable.repository.UserRepository;
import com.hirable.service.JobSeekerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobSeekerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    private User testUser;
    private JobSeeker testJobSeeker;

    @BeforeEach
    public void setUp() {
        // Create test user
        testUser = User.builder()
                .username("testseeker@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("testseeker@email.com")
                .active(true)
                .build();
        testUser = userRepository.save(testUser);

        // Create test job seeker profile
        testJobSeeker = jobSeekerService.createJobSeekerProfile(testUser);
    }

    @Test
    public void testGetProfile() throws Exception {
        mockMvc.perform(get("/api/jobseekers/" + testJobSeeker.getId() + "/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testJobSeeker.getId()));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        JobSeekerProfileDTO profileDTO = JobSeekerProfileDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .location("New York")
                .summary("Experienced developer")
                .skills(Arrays.asList("Java", "Spring"))
                .experiences(Arrays.asList())
                .educations(Arrays.asList())
                .build();

        mockMvc.perform(put("/api/jobseekers/" + testJobSeeker.getId() + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.location").value("New York"));
    }

    @Test
    public void testGetProfileNotFound() throws Exception {
        mockMvc.perform(get("/api/jobseekers/99999/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
