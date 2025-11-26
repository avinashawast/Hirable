package com.hirable.service;

import com.hirable.dto.AnalyticsDTO;
import com.hirable.entity.*;
import com.hirable.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AnalyticsServiceTest {
    @Autowired
    private AnalyticsService analyticsService;

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

    private User jobSeekerUser;
    private User recruiterUser;
    private User adminUser;
    private Job testJob;
    private JobSeeker testJobSeeker;
    private Recruiter testRecruiter;

    @BeforeEach
    public void setUp() {
        // Create test users
        jobSeekerUser = User.builder()
                .username("seeker@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("seeker@email.com")
                .active(true)
                .build();
        jobSeekerUser = userRepository.save(jobSeekerUser);

        recruiterUser = User.builder()
                .username("recruiter@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.RECRUITER)
                .email("recruiter@email.com")
                .active(true)
                .build();
        recruiterUser = userRepository.save(recruiterUser);

        adminUser = User.builder()
                .username("admin@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.ADMIN)
                .email("admin@email.com")
                .active(true)
                .build();
        adminUser = userRepository.save(adminUser);

        // Create test job seeker profile
        testJobSeeker = JobSeeker.builder()
                .user(jobSeekerUser)
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .location("New York")
                .summary("Experienced developer")
                .skills(Arrays.asList("Java", "Spring"))
                .resumeUploadedAt(LocalDateTime.now())
                .relevanceScore(0.8)
                .build();
        testJobSeeker = jobSeekerRepository.save(testJobSeeker);

        // Create test recruiter profile
        testRecruiter = Recruiter.builder()
                .user(recruiterUser)
                .firstName("Jane")
                .lastName("Smith")
                .phone("9876543210")
                .companyName("Tech Corp")
                .companyWebsite("www.techcorp.com")
                .companyDescription("A tech company")
                .industry("Technology")
                .build();
        testRecruiter = recruiterRepository.save(testRecruiter);

        // Create test job
        testJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Senior Developer")
                .description("Looking for experienced developer")
                .requiredSkills(Arrays.asList("Java", "Spring"))
                .location("New York")
                .experienceLevel("SENIOR")
                .industry("Technology")
                .status(JobStatus.APPROVED)
                .postedAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
        testJob = jobRepository.save(testJob);

        // Create test application
        Application application = Application.builder()
                .jobSeeker(testJobSeeker)
                .job(testJob)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // Create test chat
        Chat chat = Chat.builder()
                .recruiter(testRecruiter)
                .jobSeeker(testJobSeeker)
                .createdAt(LocalDateTime.now())
                .flagged(false)
                .build();
        Chat savedChat = chatRepository.save(chat);

        // Create test messages
        Message message1 = Message.builder()
                .chat(savedChat)
                .sender(recruiterUser)
                .content("Hello, are you interested?")
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message1);

        Message message2 = Message.builder()
                .chat(savedChat)
                .sender(jobSeekerUser)
                .content("Yes, I am interested!")
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();
        messageRepository.save(message2);
    }

    @Test
    public void testGetAnalytics() {
        AnalyticsDTO analytics = analyticsService.getAnalytics();

        assertNotNull(analytics);
        assertEquals(1, analytics.getTotalJobSeekers());
        assertEquals(1, analytics.getTotalRecruiters());
        assertEquals(1, analytics.getTotalAdmins());
        assertEquals(1, analytics.getTotalJobs());
        assertEquals(1, analytics.getApprovedJobs());
        assertEquals(0, analytics.getPendingJobs());
        assertEquals(1, analytics.getTotalApplications());
        assertEquals(1, analytics.getTotalChats());
        assertEquals(2, analytics.getTotalMessages());
    }

    @Test
    public void testRecruiterActivity() {
        AnalyticsDTO analytics = analyticsService.getAnalytics();

        assertNotNull(analytics.getRecruiterActivity());
        assertEquals(1, analytics.getRecruiterActivity().getTotalJobsPosted());
        assertEquals(1, analytics.getRecruiterActivity().getJobsApproved());
        assertEquals(0, analytics.getRecruiterActivity().getJobsRejected());
        assertEquals(1, analytics.getRecruiterActivity().getCandidatesContacted());
    }

    @Test
    public void testJobSeekerActivity() {
        AnalyticsDTO analytics = analyticsService.getAnalytics();

        assertNotNull(analytics.getJobSeekerActivity());
        assertEquals(1, analytics.getJobSeekerActivity().getTotalApplicationsSubmitted());
        assertEquals(1, analytics.getJobSeekerActivity().getProfilesWithResumes());
        assertEquals(1, analytics.getJobSeekerActivity().getAverageApplicationsPerSeeker());
    }

    @Test
    public void testChatActivity() {
        AnalyticsDTO analytics = analyticsService.getAnalytics();

        assertNotNull(analytics.getChatActivity());
        assertEquals(1, analytics.getChatActivity().getTotalChats());
        assertEquals(2, analytics.getChatActivity().getTotalMessages());
        assertEquals(2, analytics.getChatActivity().getAverageMessagesPerChat());
    }
}
