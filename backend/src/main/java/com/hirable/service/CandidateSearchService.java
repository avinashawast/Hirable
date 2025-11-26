package com.hirable.service;

import com.hirable.dto.CandidateSearchCriteria;
import com.hirable.dto.CandidateSearchResult;
import com.hirable.entity.JobSeeker;
import com.hirable.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateSearchService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    public List<CandidateSearchResult> searchCandidates(CandidateSearchCriteria criteria) {
        List<JobSeeker> allCandidates = jobSeekerRepository.findAll();

        return allCandidates.stream()
                .map(candidate -> {
                    double relevanceScore = calculateRelevanceScore(candidate, criteria);
                    return CandidateSearchResult.builder()
                            .candidateId(candidate.getId())
                            .firstName(candidate.getFirstName())
                            .lastName(candidate.getLastName())
                            .skills(candidate.getSkills())
                            .experience(candidate.getExperiences().size())
                            .location(candidate.getLocation())
                            .relevanceScore(relevanceScore)
                            .lastResumeUpdate(candidate.getResumeUploadedAt())
                            .resumeFilePath(candidate.getResumeFilePath())
                            .build();
                })
                .filter(result -> result.getRelevanceScore() > 0)
                .sorted((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()))
                .collect(Collectors.toList());
    }

    public double calculateRelevanceScore(JobSeeker candidate, CandidateSearchCriteria criteria) {
        double keywordScore = calculateKeywordMatch(candidate.getSkills(), criteria.getSkills());
        double recencyScore = calculateRecencyScore(candidate.getResumeUploadedAt());
        double locationScore = calculateLocationMatch(candidate.getLocation(), criteria.getLocation());

        return (keywordScore * 0.5) + (recencyScore * 0.3) + (locationScore * 0.2);
    }

    private double calculateKeywordMatch(List<String> candidateSkills, List<String> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        if (candidateSkills == null || candidateSkills.isEmpty()) {
            return 0.0;
        }

        int matchCount = 0;
        for (String required : requiredSkills) {
            for (String candidate : candidateSkills) {
                if (candidate.equalsIgnoreCase(required)) {
                    matchCount++;
                    break;
                }
            }
        }

        return (double) matchCount / requiredSkills.size();
    }

    private double calculateRecencyScore(LocalDateTime resumeUploadedAt) {
        if (resumeUploadedAt == null) {
            return 0.0;
        }

        long daysSinceUpdate = ChronoUnit.DAYS.between(resumeUploadedAt, LocalDateTime.now());

        if (daysSinceUpdate <= 7) {
            return 1.0;
        } else if (daysSinceUpdate <= 30) {
            return 0.8;
        } else if (daysSinceUpdate <= 90) {
            return 0.6;
        } else {
            return 0.4;
        }
    }

    private double calculateLocationMatch(String candidateLocation, String requiredLocation) {
        if (requiredLocation == null || requiredLocation.isEmpty()) {
            return 1.0;
        }

        if (candidateLocation == null || candidateLocation.isEmpty()) {
            return 0.0;
        }

        return candidateLocation.equalsIgnoreCase(requiredLocation) ? 1.0 : 0.5;
    }
}
