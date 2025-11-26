package com.hirable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirable.dto.JobDTO;
import com.hirable.entity.Job;
import com.hirable.entity.JobStatus;
import com.hirable.entity.Recruiter;
import com.hirable.entity.User;
import com.hirable.entity.UserRole;
import com.hirable.repository.JobRepository;
import com.hirable.repository.RecruiterRepository;
import com.hirable.repository.UserRepository;
import com.hirable.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private String adminToken;
    private String recruiterToken;
    private Recruiter recruiter;
    private Job pendingJob;

    @BeforeEach
    public void setUp() throws Exception {
        jobRepository.deleteAll();
        recruiterRepository.deleteAll();
        userRepository.deleteAll();
        authenticationService.initializeDemoUsers();

        // Get admin token
        String adminResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin@hirable.com\",\"password\":\"admin123\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        adminToken = objectMapper.readTree(adminResponse).get("token").asText();

        // Get recruiter token
        String recruiterResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"recruiter@techcorp.com\",\"password\":\"recruiter123\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        recruiterToken = objectMapper.readTree(recruiterResponse).get("token").asText();

        // Get recruiter entity
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        recruiter = recruiterRepository.findByUserId(recruiterUser.getId()).orElseThrow();

        // Create a pending job
        pendingJob = Job.builder()
                .recruiter(recruiter)
                .title("Java Developer")
                .description("Looking for experienced Java developer")
                .requiredSkills(Arrays.asList("Java", "Spring Boot"))
                .location("Bangalore")
                .experienceLevel("MID")
                .industry("IT")
                .status(JobStatus.PENDING)
                .build();
        pendingJob = jobRepository.save(pendingJob);
    }

    @Test
    public void testGetAllJobs() throws Exception {
        mockMvc.perform(get("/api/jobs/all")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetJobsByStatus() throws Exception {
        mockMvc.perform(get("/api/jobs/status/PENDING")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    public void testApproveJob() throws Exception {
        mockMvc.perform(put("/api/jobs/" + pendingJob.getId() + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.approvedAt").exists());

        Job approvedJob = jobRepository.findById(pendingJob.getId()).orElseThrow();
        assert approvedJob.getStatus() == JobStatus.APPROVED;
        assert approvedJob.getApprovedAt() != null;
    }

    @Test
    public void testRejectJob() throws Exception {
        String reason = "Does not meet quality standards";

        mockMvc.perform(put("/api/jobs/" + pendingJob.getId() + "/reject?reason=" + reason)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.rejectionReason").value(reason));

        Job rejectedJob = jobRepository.findById(pendingJob.getId()).orElseThrow();
        assert rejectedJob.getStatus() == JobStatus.REJECTED;
        assert rejectedJob.getRejectionReason().equals(reason);
    }

    @Test
    public void testRemoveJob() throws Exception {
        // First approve the job
        pendingJob.setStatus(JobStatus.APPROVED);
        pendingJob.setApprovedAt(LocalDateTime.now());
        jobRepository.save(pendingJob);

        mockMvc.perform(delete("/api/jobs/" + pendingJob.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        Job removedJob = jobRepository.findById(pendingJob.getId()).orElseThrow();
        assert removedJob.getStatus() == JobStatus.REMOVED;
    }

    @Test
    public void testGetJobById() throws Exception {
        mockMvc.perform(get("/api/jobs/" + pendingJob.getId())
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pendingJob.getId()))
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testGetPendingJobs() throws Exception {
        mockMvc.perform(get("/api/jobs/pending")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    public void testCreateJob() throws Exception {
        JobDTO newJob = JobDTO.builder()
                .title("Python Developer")
                .description("Looking for Python developer")
                .requiredSkills(Arrays.asList("Python", "Django"))
                .location("Bangalore")
                .experienceLevel("ENTRY")
                .industry("IT")
                .build();

        mockMvc.perform(post("/api/recruiters/" + recruiter.getId() + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Python Developer"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testUpdateJob() throws Exception {
        JobDTO updateJob = JobDTO.builder()
                .title("Senior Java Developer")
                .description("Looking for senior Java developer")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "Microservices"))
                .location("Bangalore")
                .experienceLevel("SENIOR")
                .industry("IT")
                .build();

        mockMvc.perform(put("/api/recruiters/" + recruiter.getId() + "/jobs/" + pendingJob.getId())
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateJob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Java Developer"))
                .andExpect(jsonPath("$.experienceLevel").value("SENIOR"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        mockMvc.perform(delete("/api/recruiters/" + recruiter.getId() + "/jobs/" + pendingJob.getId())
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isNoContent());

        assert jobRepository.findById(pendingJob.getId()).isEmpty();
    }
}
