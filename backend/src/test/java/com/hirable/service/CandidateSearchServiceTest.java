package com.hirable.service;

import com.hirable.dto.CandidateSearchCriteria;
import com.hirable.dto.CandidateSearchResult;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CandidateSearchServiceTest {
    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    private JobSeeker candidate1;
    private JobSeeker candidate2;
    private JobSeeker candidate3;

    @BeforeEach
    public void setUp() {
        // Create test users
        User user1 = User.builder()
                .username("candidate1@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("candidate1@email.com")
                .active(true)
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .username("candidate2@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("candidate2@email.com")
                .active(true)
                .build();
        user2 = userRepository.save(user2);

        User user3 = User.builder()
                .username("candidate3@email.com")
                .passwordHash("hashedpassword")
                .role(UserRole.JOB_SEEKER)
                .email("candidate3@email.com")
                .active(true)
                .build();
        user3 = userRepository.save(user3);

        // Create test job seekers with different skills and locations
        candidate1 = JobSeeker.builder()
                .user(user1)
                .firstName("Alice")
                .lastName("Johnson")
                .location("New York")
                .skills(Arrays.asList("Java", "Spring", "PostgreSQL"))
                .resumeUploadedAt(LocalDateTime.now())
                .relevanceScore(1.0)
                .build();
        candidate1 = jobSeekerRepository.save(candidate1);

        candidate2 = JobSeeker.builder()
                .user(user2)
                .firstName("Bob")
                .lastName("Smith")
                .location("San Francisco")
                .skills(Arrays.asList("Python", "Django", "MySQL"))
                .resumeUploadedAt(LocalDateTime.now().minusDays(15))
                .relevanceScore(0.8)
                .build();
        candidate2 = jobSeekerRepository.save(candidate2);

        candidate3 = JobSeeker.builder()
                .user(user3)
                .firstName("Charlie")
                .lastName("Brown")
                .location("New York")
                .skills(Arrays.asList("Java", "Kotlin", "MongoDB"))
                .resumeUploadedAt(LocalDateTime.now().minusDays(100))
                .relevanceScore(0.4)
                .build();
        candidate3 = jobSeekerRepository.save(candidate3);
    }

    @Test
    public void testSearchCandidatesBySkills() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .skills(Arrays.asList("Java"))
                .build();

        List<CandidateSearchResult> results = candidateSearchService.searchCandidates(criteria);

        assertNotNull(results);
        assertTrue(results.size() >= 2);
        assertTrue(results.stream().anyMatch(r -> r.getFirstName().equals("Alice")));
        assertTrue(results.stream().anyMatch(r -> r.getFirstName().equals("Charlie")));
    }

    @Test
    public void testSearchCandidatesByLocation() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .location("New York")
                .build();

        List<CandidateSearchResult> results = candidateSearchService.searchCandidates(criteria);

        assertNotNull(results);
        assertTrue(results.size() >= 2);
        assertTrue(results.stream().anyMatch(r -> r.getFirstName().equals("Alice")));
        assertTrue(results.stream().anyMatch(r -> r.getFirstName().equals("Charlie")));
    }

    @Test
    public void testRelevanceScoreSorting() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .skills(Arrays.asList("Java"))
                .build();

        List<CandidateSearchResult> results = candidateSearchService.searchCandidates(criteria);

        assertNotNull(results);
        assertTrue(results.size() >= 2);
        // Alice should rank higher due to more recent resume
        assertTrue(results.get(0).getRelevanceScore() >= results.get(1).getRelevanceScore());
    }

    @Test
    public void testKeywordMatchCalculation() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .skills(Arrays.asList("Java", "Spring"))
                .build();

        double score = candidateSearchService.calculateRelevanceScore(candidate1, criteria);

        assertTrue(score > 0);
        assertTrue(score <= 1.0);
    }

    @Test
    public void testLocationMatchCalculation() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .location("New York")
                .build();

        double score = candidateSearchService.calculateRelevanceScore(candidate1, criteria);

        assertTrue(score > 0);
    }

    @Test
    public void testEmptySearchResults() {
        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .skills(Arrays.asList("NonExistentSkill"))
                .build();

        List<CandidateSearchResult> results = candidateSearchService.searchCandidates(criteria);

        assertNotNull(results);
        assertEquals(0, results.size());
    }
}
