package com.hirable.controller;

import com.hirable.dto.AnalyticsDTO;
import com.hirable.entity.*;
import com.hirable.repository.*;
import com.hirable.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AnalyticsService analyticsService;

    private User adminUser;

    @BeforeEach
    public void setUp() {
        // Create admin user
        adminUser = User.builder()
                .username("admin@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.ADMIN)
                .email("admin@email.com")
                .active(true)
                .build();
        userRepository.save(adminUser);

        // Create test job seeker
        User jobSeekerUser = User.builder()
                .username("seeker@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("seeker@email.com")
                .active(true)
                .build();
        jobSeekerUser = userRepository.save(jobSeekerUser);

        JobSeeker jobSeeker = JobSeeker.builder()
                .user(jobSeekerUser)
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .location("New York")
                .summary("Developer")
                .skills(Arrays.asList("Java"))
                .resumeUploadedAt(LocalDateTime.now())
                .relevanceScore(0.8)
                .build();
        jobSeekerRepository.save(jobSeeker);

        // Create test recruiter
        User recruiterUser = User.builder()
                .username("recruiter@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.RECRUITER)
                .email("recruiter@email.com")
                .active(true)
                .build();
        recruiterUser = userRepository.save(recruiterUser);

        Recruiter recruiter = Recruiter.builder()
                .user(recruiterUser)
                .firstName("Jane")
                .lastName("Smith")
                .phone("9876543210")
                .companyName("Tech Corp")
                .companyWebsite("www.techcorp.com")
                .companyDescription("A tech company")
                .industry("Technology")
                .build();
        recruiterRepository.save(recruiter);

        // Create test job
        Job job = Job.builder()
                .recruiter(recruiter)
                .title("Senior Developer")
                .description("Looking for experienced developer")
                .requiredSkills(Arrays.asList("Java"))
                .location("New York")
                .experienceLevel("SENIOR")
                .industry("Technology")
                .status(JobStatus.APPROVED)
                .postedAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
        jobRepository.save(job);

        // Create test application
        Application application = Application.builder()
                .jobSeeker(jobSeeker)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // Create test chat
        Chat chat = Chat.builder()
                .recruiter(recruiter)
                .jobSeeker(jobSeeker)
                .createdAt(LocalDateTime.now())
                .flagged(false)
                .build();
        Chat savedChat = chatRepository.save(chat);

        // Create test messages
        Message message = Message.builder()
                .chat(savedChat)
                .sender(recruiterUser)
                .content("Hello")
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAnalyticsEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/analytics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalJobSeekers", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalRecruiters", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalAdmins", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalJobs", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.approvedJobs", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalApplications", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalChats", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalMessages", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.recruiterActivity", notNullValue()))
                .andExpect(jsonPath("$.jobSeekerActivity", notNullValue()))
                .andExpect(jsonPath("$.chatActivity", notNullValue()));
    }

    @Test
    public void testGetAnalyticsEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/analytics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "JOB_SEEKER")
    public void testGetAnalyticsEndpointForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/analytics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
