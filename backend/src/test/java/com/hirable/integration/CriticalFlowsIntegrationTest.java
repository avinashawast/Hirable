package com.hirable.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirable.dto.*;
import com.hirable.entity.*;
import com.hirable.repository.*;
import com.hirable.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for critical user flows in Hirable platform.
 * Tests cover authentication, job posting, candidate search, chat, and file upload.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CriticalFlowsIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private String adminToken;
    private String recruiterToken;
    private String jobSeekerToken;
    private Long recruiterId;
    private Long jobSeekerId;
    private Long recruiterUserId;
    private Long jobId;

    @BeforeEach
    public void setUp() throws Exception {
        // Clean up all data
        messageRepository.deleteAll();
        chatRepository.deleteAll();
        applicationRepository.deleteAll();
        jobRepository.deleteAll();
        jobSeekerRepository.deleteAll();
        recruiterRepository.deleteAll();
        userRepository.deleteAll();

        // Initialize demo users
        authenticationService.initializeDemoUsers();

        // Get tokens for all roles
        adminToken = getToken("admin@hirable.com", "admin123");
        recruiterToken = getToken("recruiter@techcorp.com", "recruiter123");
        jobSeekerToken = getToken("jobseeker@email.com", "jobseeker123");

        // Get recruiter and job seeker IDs
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        recruiterUserId = recruiterUser.getId();
        Recruiter recruiter = recruiterRepository.findByUserId(recruiterUser.getId()).orElseThrow();
        recruiterId = recruiter.getId();

        User jobSeekerUser = userRepository.findByUsername("jobseeker@email.com").orElseThrow();
        JobSeeker jobSeeker = jobSeekerRepository.findByUserId(jobSeekerUser.getId()).orElseThrow();
        jobSeekerId = jobSeeker.getId();
    }

    // ==================== Authentication Flow Tests ====================

    @Test
    public void testAuthenticationFlowWithValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin@hirable.com", "admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.userId").isNumber())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(responseBody, LoginResponse.class);

        assertNotNull(response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertTrue(response.getUserId() > 0);
    }

    @Test
    public void testAuthenticationFlowWithInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin@hirable.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAuthenticationFlowWithNonExistentUser() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent@email.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    // ==================== Job Posting Creation and Approval Workflow ====================

    @Test
    public void testJobPostingCreationAndApprovalWorkflow() throws Exception {
        // Step 1: Recruiter creates a job posting
        JobDTO newJob = JobDTO.builder()
                .title("Senior Java Developer")
                .description("Looking for experienced Java developer with Spring Boot expertise")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "PostgreSQL"))
                .location("Bangalore")
                .experienceLevel("SENIOR")
                .industry("IT")
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Java Developer"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        JobDTO createdJob = objectMapper.readValue(createResponse, JobDTO.class);
        jobId = createdJob.getId();

        assertNotNull(jobId);
        assertEquals("PENDING", createdJob.getStatus());

        // Step 2: Admin retrieves pending jobs
        mockMvc.perform(get("/api/jobs/pending")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        // Step 3: Admin approves the job
        MvcResult approveResult = mockMvc.perform(put("/api/jobs/" + jobId + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.approvedAt").exists())
                .andReturn();

        String approveResponse = approveResult.getResponse().getContentAsString();
        JobDTO approvedJob = objectMapper.readValue(approveResponse, JobDTO.class);

        assertEquals("APPROVED", approvedJob.getStatus());
        assertNotNull(approvedJob.getApprovedAt());

        // Verify in database
        Job dbJob = jobRepository.findById(jobId).orElseThrow();
        assertEquals(JobStatus.APPROVED, dbJob.getStatus());
        assertNotNull(dbJob.getApprovedAt());
    }

    @Test
    public void testJobPostingRejectionWorkflow() throws Exception {
        // Create a pending job
        JobDTO newJob = JobDTO.builder()
                .title("Python Developer")
                .description("Looking for Python developer")
                .requiredSkills(Arrays.asList("Python", "Django"))
                .location("Bangalore")
                .experienceLevel("MID")
                .industry("IT")
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJob)))
                .andExpect(status().isOk())
                .andReturn();

        JobDTO createdJob = objectMapper.readValue(createResult.getResponse().getContentAsString(), JobDTO.class);
        Long jobId = createdJob.getId();

        // Admin rejects the job
        String rejectionReason = "Does not meet quality standards";
        mockMvc.perform(put("/api/jobs/" + jobId + "/reject?reason=" + rejectionReason)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.rejectionReason").value(rejectionReason));

        // Verify in database
        Job dbJob = jobRepository.findById(jobId).orElseThrow();
        assertEquals(JobStatus.REJECTED, dbJob.getStatus());
        assertEquals(rejectionReason, dbJob.getRejectionReason());
    }

    // ==================== Candidate Search with Relevance Ranking ====================

    @Test
    public void testCandidateSearchWithRelevanceRanking() throws Exception {
        // Setup: Create multiple candidates with different skills and resume upload times
        User user1 = User.builder()
                .username("candidate1@test.com")
                .passwordHash("hash")
                .role(UserRole.JOB_SEEKER)
                .email("candidate1@test.com")
                .active(true)
                .build();
        user1 = userRepository.save(user1);

        JobSeeker candidate1 = JobSeeker.builder()
                .user(user1)
                .firstName("Alice")
                .lastName("Johnson")
                .location("Bangalore")
                .skills(Arrays.asList("Java", "Spring Boot", "PostgreSQL"))
                .resumeUploadedAt(LocalDateTime.now())
                .build();
        candidate1 = jobSeekerRepository.save(candidate1);

        User user2 = User.builder()
                .username("candidate2@test.com")
                .passwordHash("hash")
                .role(UserRole.JOB_SEEKER)
                .email("candidate2@test.com")
                .active(true)
                .build();
        user2 = userRepository.save(user2);

        JobSeeker candidate2 = JobSeeker.builder()
                .user(user2)
                .firstName("Bob")
                .lastName("Smith")
                .location("Mumbai")
                .skills(Arrays.asList("Java", "Microservices"))
                .resumeUploadedAt(LocalDateTime.now().minusDays(30))
                .build();
        candidate2 = jobSeekerRepository.save(candidate2);

        // Search for candidates with Java skill
        mockMvc.perform(get("/api/candidates/search?skills=Java&location=Bangalore")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    public void testCandidateSearchBySkills() throws Exception {
        // Search for candidates with specific skills
        mockMvc.perform(get("/api/candidates/search?skills=Java,Spring")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ==================== Chat Message Sending and Retrieval ====================

    @Test
    public void testChatCreationAndRetrieval() throws Exception {
        // Step 1: Create a chat between recruiter and job seeker
        mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.recruiterName").exists())
                .andExpect(jsonPath("$.jobSeekerName").exists());

        // Step 2: Retrieve recruiter's chats
        mockMvc.perform(get("/api/chats/" + recruiterUserId)
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Step 3: Verify chat persisted in database
        List<Chat> chats = chatRepository.findByUserId(recruiterUserId);
        assertEquals(1, chats.size());
    }

    @Test
    public void testChatMessagePersistence() throws Exception {
        // Create a chat
        MvcResult createChatResult = mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andReturn();

        ChatDTO createdChat = objectMapper.readValue(createChatResult.getResponse().getContentAsString(), ChatDTO.class);
        Long chatId = createdChat.getId();

        // Manually create messages in database (simulating WebSocket message sending)
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();

        for (int i = 1; i <= 3; i++) {
            Message message = Message.builder()
                    .chat(chat)
                    .sender(recruiterUser)
                    .content("Message " + i)
                    .sentAt(LocalDateTime.now())
                    .read(false)
                    .build();
            messageRepository.save(message);
        }

        // Retrieve all messages
        mockMvc.perform(get("/api/chats/" + chatId + "/messages")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].content").value("Message 1"))
                .andExpect(jsonPath("$[1].content").value("Message 2"))
                .andExpect(jsonPath("$[2].content").value("Message 3"));

        // Verify messages persisted in database
        List<Message> messages = messageRepository.findByChatId(chatId);
        assertEquals(3, messages.size());
    }

    // ==================== File Upload Tests ====================

    @Test
    public void testFileUploadWithPdfFormat() throws Exception {
        // Create a mock PDF file
        byte[] pdfContent = "%PDF-1.4\n%test pdf content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                pdfContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk());

        // Verify resume is stored
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow();
        assertNotNull(jobSeeker.getResumeFilePath());
        assertTrue(jobSeeker.getResumeFilePath().endsWith(".pdf"));
    }

    @Test
    public void testFileUploadWithDocFormat() throws Exception {
        // Create a mock DOC file
        byte[] docContent = "test doc content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.doc",
                "application/msword",
                docContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk());

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow();
        assertNotNull(jobSeeker.getResumeFilePath());
        assertTrue(jobSeeker.getResumeFilePath().endsWith(".doc"));
    }

    @Test
    public void testFileUploadWithDocxFormat() throws Exception {
        // Create a mock DOCX file
        byte[] docxContent = "test docx content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                docxContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk());

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow();
        assertNotNull(jobSeeker.getResumeFilePath());
        assertTrue(jobSeeker.getResumeFilePath().endsWith(".docx"));
    }

    @Test
    public void testFileUploadWithInvalidFormat() throws Exception {
        // Try to upload an invalid file format
        byte[] txtContent = "test text content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.txt",
                "text/plain",
                txtContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFileUploadExceedingSizeLimit() throws Exception {
        // Create a file exceeding 5MB limit
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                largeContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFileUploadWithEmptyFile() throws Exception {
        // Try to upload an empty file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                new byte[0]
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testResumeDownload() throws Exception {
        // First upload a resume
        byte[] pdfContent = "%PDF-1.4\n%test pdf content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                pdfContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(file)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk());

        // Download the resume
        mockMvc.perform(get("/api/jobseekers/" + jobSeekerId + "/resume/download")
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
    }

    // ==================== Helper Methods ====================

    private String getToken(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest(username, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(responseBody, LoginResponse.class);
        return response.getToken();
    }
}
