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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end tests for complete user journeys in Hirable platform.
 * Tests simulate realistic user workflows across Job Seeker, Recruiter, and Admin roles.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserJourneyE2ETest {
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
    private NotificationRepository notificationRepository;

    @Autowired
    private ShortlistRepository shortlistRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private String adminToken;
    private String recruiterToken;
    private String jobSeekerToken;
    private Long recruiterId;
    private Long jobSeekerId;
    private Long recruiterUserId;
    private Long jobSeekerId_UserId;

    @BeforeEach
    public void setUp() throws Exception {
        // Clean up all data
        messageRepository.deleteAll();
        chatRepository.deleteAll();
        shortlistRepository.deleteAll();
        applicationRepository.deleteAll();
        notificationRepository.deleteAll();
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
        jobSeekerId_UserId = jobSeekerUser.getId();
        JobSeeker jobSeeker = jobSeekerRepository.findByUserId(jobSeekerUser.getId()).orElseThrow();
        jobSeekerId = jobSeeker.getId();
    }

    // ==================== Job Seeker Journey ====================

    /**
     * E2E Test: Job Seeker complete journey
     * Flow: Login -> Upload Resume -> Search Jobs -> Apply -> Receive Chat Message
     */
    @Test
    public void testJobSeekerCompleteJourney() throws Exception {
        // Step 1: Job Seeker logs in
        LoginRequest loginRequest = new LoginRequest("jobseeker@email.com", "jobseeker123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("JOB_SEEKER"))
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                LoginResponse.class
        );
        String jobSeekerToken = loginResponse.getToken();
        assertNotNull(jobSeekerToken);

        // Step 2: Job Seeker uploads resume
        byte[] pdfContent = "%PDF-1.4\n%test pdf content".getBytes();
        MockMultipartFile resumeFile = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                pdfContent
        );

        mockMvc.perform(multipart("/api/jobseekers/" + jobSeekerId + "/resume")
                .file(resumeFile)
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk());

        // Verify resume uploaded
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow();
        assertNotNull(jobSeeker.getResumeFilePath());

        // Step 3: Recruiter creates and posts a job
        JobDTO jobDTO = JobDTO.builder()
                .title("Senior Java Developer")
                .description("Looking for experienced Java developer")
                .requiredSkills(Arrays.asList("Java", "Spring Boot"))
                .location("Bangalore")
                .experienceLevel("SENIOR")
                .industry("IT")
                .build();

        MvcResult jobCreateResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JobDTO createdJob = objectMapper.readValue(
                jobCreateResult.getResponse().getContentAsString(),
                JobDTO.class
        );
        Long jobId = createdJob.getId();

        // Step 4: Admin approves the job
        mockMvc.perform(put("/api/jobs/" + jobId + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // Step 5: Job Seeker searches for jobs
        mockMvc.perform(get("/api/jobs?keyword=Java&location=Bangalore")
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Step 6: Job Seeker applies to the job
        mockMvc.perform(post("/api/jobseekers/" + jobSeekerId + "/applications")
                .header("Authorization", "Bearer " + jobSeekerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"jobId\":" + jobId + "}"))
                .andExpect(status().isOk());

        // Verify application created
        List<Application> applications = applicationRepository.findByJobSeekerId(jobSeekerId);
        assertEquals(1, applications.size());

        // Step 7: Recruiter initiates chat with Job Seeker
        MvcResult chatResult = mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andReturn();

        ChatDTO chatDTO = objectMapper.readValue(
                chatResult.getResponse().getContentAsString(),
                ChatDTO.class
        );
        Long chatId = chatDTO.getId();

        // Step 8: Simulate recruiter sending message (via database)
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        Message message = Message.builder()
                .chat(chat)
                .sender(recruiterUser)
                .content("Great resume! Interested in discussing this opportunity?")
                .sentAt(java.time.LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message);

        // Step 9: Job Seeker retrieves chat messages
        mockMvc.perform(get("/api/chats/" + chatId + "/messages")
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].content").value("Great resume! Interested in discussing this opportunity?"));

        // Verify complete journey
        assertTrue(applications.size() > 0);
        assertTrue(messageRepository.findByChatId(chatId).size() > 0);
    }


    // ==================== Recruiter Journey ====================

    /**
     * E2E Test: Recruiter complete journey
     * Flow: Login -> Post Job -> Search Candidates -> Initiate Chat -> Shortlist Candidate
     */
    @Test
    public void testRecruiterCompleteJourney() throws Exception {
        // Step 1: Recruiter logs in
        LoginRequest loginRequest = new LoginRequest("recruiter@techcorp.com", "recruiter123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("RECRUITER"))
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                LoginResponse.class
        );
        String recruiterToken = loginResponse.getToken();
        assertNotNull(recruiterToken);

        // Step 2: Recruiter creates a job posting
        JobDTO jobDTO = JobDTO.builder()
                .title("Full Stack Developer")
                .description("Looking for experienced full stack developer")
                .requiredSkills(Arrays.asList("Java", "Angular", "PostgreSQL"))
                .location("Bangalore")
                .experienceLevel("MID")
                .industry("IT")
                .build();

        MvcResult jobCreateResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JobDTO createdJob = objectMapper.readValue(
                jobCreateResult.getResponse().getContentAsString(),
                JobDTO.class
        );
        Long jobId = createdJob.getId();
        assertEquals("PENDING", createdJob.getStatus());

        // Step 3: Admin approves the job
        mockMvc.perform(put("/api/jobs/" + jobId + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // Step 4: Recruiter searches for candidates
        mockMvc.perform(get("/api/candidates/search?skills=Java&location=Bangalore")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Step 5: Recruiter initiates chat with Job Seeker
        MvcResult chatResult = mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andReturn();

        ChatDTO chatDTO = objectMapper.readValue(
                chatResult.getResponse().getContentAsString(),
                ChatDTO.class
        );
        Long chatId = chatDTO.getId();
        assertNotNull(chatId);

        // Step 6: Recruiter sends message to candidate
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        Message message = Message.builder()
                .chat(chat)
                .sender(recruiterUser)
                .content("We have an exciting opportunity for you!")
                .sentAt(java.time.LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message);

        // Step 7: Recruiter shortlists the candidate
        mockMvc.perform(post("/api/recruiters/" + recruiterId + "/shortlist")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk());

        // Verify shortlist created
        List<Shortlist> shortlists = shortlistRepository.findByRecruiterId(recruiterId);
        assertEquals(1, shortlists.size());
        assertEquals(jobSeekerId, shortlists.get(0).getJobSeeker().getId());

        // Step 8: Recruiter views shortlist
        mockMvc.perform(get("/api/recruiters/" + recruiterId + "/shortlist")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Verify complete journey
        assertTrue(jobRepository.findById(jobId).isPresent());
        assertTrue(chatRepository.findById(chatId).isPresent());
        assertTrue(shortlists.size() > 0);
    }


    // ==================== Admin Journey ====================

    /**
     * E2E Test: Admin complete journey
     * Flow: Login -> Approve Job -> View Analytics -> Moderate Chat
     */
    @Test
    public void testAdminCompleteJourney() throws Exception {
        // Step 1: Admin logs in
        LoginRequest loginRequest = new LoginRequest("admin@hirable.com", "admin123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                LoginResponse.class
        );
        String adminToken = loginResponse.getToken();
        assertNotNull(adminToken);

        // Step 2: Recruiter creates a job posting
        JobDTO jobDTO = JobDTO.builder()
                .title("DevOps Engineer")
                .description("Looking for DevOps engineer with Kubernetes experience")
                .requiredSkills(Arrays.asList("Kubernetes", "Docker", "AWS"))
                .location("Bangalore")
                .experienceLevel("SENIOR")
                .industry("IT")
                .build();

        MvcResult jobCreateResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JobDTO createdJob = objectMapper.readValue(
                jobCreateResult.getResponse().getContentAsString(),
                JobDTO.class
        );
        Long jobId = createdJob.getId();

        // Step 3: Admin retrieves pending jobs
        mockMvc.perform(get("/api/jobs/pending")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Step 4: Admin approves the job
        mockMvc.perform(put("/api/jobs/" + jobId + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        // Verify job approved
        Job approvedJob = jobRepository.findById(jobId).orElseThrow();
        assertEquals(JobStatus.APPROVED, approvedJob.getStatus());

        // Step 5: Admin views analytics
        mockMvc.perform(get("/api/admin/analytics")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalJobSeekers").isNumber())
                .andExpect(jsonPath("$.totalRecruiters").isNumber())
                .andExpect(jsonPath("$.totalJobs").isNumber());

        // Step 6: Create a chat for moderation
        MvcResult chatResult = mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andReturn();

        ChatDTO chatDTO = objectMapper.readValue(
                chatResult.getResponse().getContentAsString(),
                ChatDTO.class
        );
        Long chatId = chatDTO.getId();

        // Step 7: Add messages to chat
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        Message message = Message.builder()
                .chat(chat)
                .sender(recruiterUser)
                .content("Test message for moderation")
                .sentAt(java.time.LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message);

        // Step 8: Admin views all chats
        mockMvc.perform(get("/api/chats/all")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Step 9: Admin flags a chat
        mockMvc.perform(put("/api/chats/" + chatId + "/flag")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reason\":\"Inappropriate content\"}"))
                .andExpect(status().isOk());

        // Verify chat flagged
        Chat flaggedChat = chatRepository.findById(chatId).orElseThrow();
        assertTrue(flaggedChat.isFlagged());
        assertEquals("Inappropriate content", flaggedChat.getFlagReason());

        // Verify complete journey
        assertTrue(jobRepository.findById(jobId).isPresent());
        assertTrue(chatRepository.findById(chatId).isPresent());
    }


    // ==================== Responsive Design Tests ====================

    /**
     * Test responsive design across different viewports
     * Note: Backend API tests don't directly test UI responsiveness,
     * but we verify that all endpoints work correctly for mobile, tablet, and desktop clients
     */
    @Test
    public void testResponsiveDesignMobileViewport() throws Exception {
        // Simulate mobile client accessing API endpoints
        // Mobile clients should receive the same API responses as desktop clients

        // Test login on mobile
        LoginRequest loginRequest = new LoginRequest("jobseeker@email.com", "jobseeker123");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Mobile Safari")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Test job search on mobile
        mockMvc.perform(get("/api/jobs?keyword=Java")
                .header("Authorization", "Bearer " + jobSeekerToken)
                .header("User-Agent", "Mobile Safari"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Test profile retrieval on mobile
        mockMvc.perform(get("/api/jobseekers/" + jobSeekerId + "/profile")
                .header("Authorization", "Bearer " + jobSeekerToken)
                .header("User-Agent", "Mobile Safari"))
                .andExpect(status().isOk());
    }

    @Test
    public void testResponsiveDesignTabletViewport() throws Exception {
        // Simulate tablet client accessing API endpoints
        LoginRequest loginRequest = new LoginRequest("recruiter@techcorp.com", "recruiter123");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "iPad Safari")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Test candidate search on tablet
        mockMvc.perform(get("/api/candidates/search?skills=Java")
                .header("Authorization", "Bearer " + recruiterToken)
                .header("User-Agent", "iPad Safari"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Test recruiter dashboard data on tablet
        mockMvc.perform(get("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .header("User-Agent", "iPad Safari"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testResponsiveDesignDesktopViewport() throws Exception {
        // Simulate desktop client accessing API endpoints
        LoginRequest loginRequest = new LoginRequest("admin@hirable.com", "admin123");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Chrome Desktop")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Test admin analytics on desktop
        mockMvc.perform(get("/api/admin/analytics")
                .header("Authorization", "Bearer " + adminToken)
                .header("User-Agent", "Chrome Desktop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalJobSeekers").isNumber());

        // Test user management on desktop
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + adminToken)
                .header("User-Agent", "Chrome Desktop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ==================== Multi-Step User Journeys ====================

    /**
     * Test complete job application workflow with multiple steps
     */
    @Test
    public void testCompleteJobApplicationWorkflow() throws Exception {
        // Step 1: Recruiter posts a job
        JobDTO jobDTO = JobDTO.builder()
                .title("Backend Engineer")
                .description("Looking for backend engineer")
                .requiredSkills(Arrays.asList("Java", "Spring Boot"))
                .location("Bangalore")
                .experienceLevel("MID")
                .industry("IT")
                .build();

        MvcResult jobResult = mockMvc.perform(post("/api/recruiters/" + recruiterId + "/jobs")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JobDTO createdJob = objectMapper.readValue(
                jobResult.getResponse().getContentAsString(),
                JobDTO.class
        );
        Long jobId = createdJob.getId();

        // Step 2: Admin approves job
        mockMvc.perform(put("/api/jobs/" + jobId + "/approve")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // Step 3: Job Seeker searches and finds the job
        mockMvc.perform(get("/api/jobs?keyword=Backend")
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Backend Engineer"));

        // Step 4: Job Seeker applies
        mockMvc.perform(post("/api/jobseekers/" + jobSeekerId + "/applications")
                .header("Authorization", "Bearer " + jobSeekerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"jobId\":" + jobId + "}"))
                .andExpect(status().isOk());

        // Step 5: Verify application created
        List<Application> applications = applicationRepository.findByJobSeekerId(jobSeekerId);
        assertEquals(1, applications.size());
        assertEquals(jobId, applications.get(0).getJob().getId());
    }

    /**
     * Test candidate search and shortlist workflow
     */
    @Test
    public void testCandidateSearchAndShortlistWorkflow() throws Exception {
        // Step 1: Recruiter searches for candidates
        mockMvc.perform(get("/api/candidates/search?skills=Java&location=Bangalore")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Step 2: Recruiter shortlists a candidate
        mockMvc.perform(post("/api/recruiters/" + recruiterId + "/shortlist")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk());

        // Step 3: Verify shortlist
        List<Shortlist> shortlists = shortlistRepository.findByRecruiterId(recruiterId);
        assertEquals(1, shortlists.size());

        // Step 4: Recruiter views shortlist
        mockMvc.perform(get("/api/recruiters/" + recruiterId + "/shortlist")
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        // Step 5: Recruiter removes from shortlist
        mockMvc.perform(delete("/api/recruiters/" + recruiterId + "/shortlist/" + jobSeekerId)
                .header("Authorization", "Bearer " + recruiterToken))
                .andExpect(status().isOk());

        // Verify removed
        List<Shortlist> updatedShortlists = shortlistRepository.findByRecruiterId(recruiterId);
        assertEquals(0, updatedShortlists.size());
    }

    /**
     * Test chat conversation workflow
     */
    @Test
    public void testChatConversationWorkflow() throws Exception {
        // Step 1: Create chat
        MvcResult chatResult = mockMvc.perform(post("/api/chats")
                .header("Authorization", "Bearer " + recruiterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recruiterId\":" + recruiterId + ",\"jobSeekerId\":" + jobSeekerId + "}"))
                .andExpect(status().isOk())
                .andReturn();

        ChatDTO chatDTO = objectMapper.readValue(
                chatResult.getResponse().getContentAsString(),
                ChatDTO.class
        );
        Long chatId = chatDTO.getId();

        // Step 2: Add multiple messages
        User recruiterUser = userRepository.findByUsername("recruiter@techcorp.com").orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();

        for (int i = 1; i <= 5; i++) {
            Message message = Message.builder()
                    .chat(chat)
                    .sender(recruiterUser)
                    .content("Message " + i)
                    .sentAt(java.time.LocalDateTime.now())
                    .read(false)
                    .build();
            messageRepository.save(message);
        }

        // Step 3: Retrieve chat messages
        mockMvc.perform(get("/api/chats/" + chatId + "/messages")
                .header("Authorization", "Bearer " + jobSeekerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        // Step 4: Verify message persistence
        List<Message> messages = messageRepository.findByChatId(chatId);
        assertEquals(5, messages.size());
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
