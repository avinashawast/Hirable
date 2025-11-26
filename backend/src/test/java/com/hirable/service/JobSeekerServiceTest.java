package com.hirable.service;

import com.hirable.dto.JobSeekerProfileDTO;
import com.hirable.entity.JobSeeker;
import com.hirable.entity.User;
import com.hirable.entity.UserRole;
import com.hirable.repository.JobSeekerRepository;
import com.hirable.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JobSeekerServiceTest {
    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

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
    public void testCreateJobSeekerProfile() {
        assertNotNull(testJobSeeker.getId());
        assertEquals(testUser.getId(), testJobSeeker.getUser().getId());
        assertEquals(0.0, testJobSeeker.getRelevanceScore());
    }

    @Test
    public void testGetProfile() {
        JobSeekerProfileDTO profile = jobSeekerService.getProfile(testJobSeeker.getId());
        assertNotNull(profile);
        assertEquals(testJobSeeker.getId(), profile.getId());
    }

    @Test
    public void testUpdateProfile() {
        JobSeekerProfileDTO profileDTO = JobSeekerProfileDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .location("New York")
                .summary("Experienced software developer")
                .skills(Arrays.asList("Java", "Spring", "PostgreSQL"))
                .experiences(Arrays.asList())
                .educations(Arrays.asList())
                .build();

        JobSeekerProfileDTO updated = jobSeekerService.updateProfile(testJobSeeker.getId(), profileDTO);

        assertEquals("John", updated.getFirstName());
        assertEquals("Doe", updated.getLastName());
        assertEquals("New York", updated.getLocation());
        assertEquals(3, updated.getSkills().size());
        assertTrue(updated.getSkills().contains("Java"));
    }

    @Test
    public void testUpdateRelevanceScore() {
        testJobSeeker.setResumeUploadedAt(java.time.LocalDateTime.now());
        jobSeekerRepository.save(testJobSeeker);

        jobSeekerService.updateRelevanceScore(testJobSeeker);

        assertEquals(1.0, testJobSeeker.getRelevanceScore());
    }
}
