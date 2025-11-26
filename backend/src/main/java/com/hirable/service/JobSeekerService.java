package com.hirable.service;

import com.hirable.dto.JobSeekerProfileDTO;
import com.hirable.dto.ParsedResumeData;
import com.hirable.entity.JobSeeker;
import com.hirable.entity.User;
import com.hirable.exception.UserNotFoundException;
import com.hirable.repository.JobSeekerRepository;
import com.hirable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class JobSeekerService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeService resumeService;

    public JobSeekerProfileDTO getProfile(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new UserNotFoundException(jobSeekerId));
        return mapToDTO(jobSeeker);
    }

    public JobSeekerProfileDTO updateProfile(Long jobSeekerId, JobSeekerProfileDTO profileDTO) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new UserNotFoundException(jobSeekerId));

        jobSeeker.setFirstName(profileDTO.getFirstName());
        jobSeeker.setLastName(profileDTO.getLastName());
        jobSeeker.setPhone(profileDTO.getPhone());
        jobSeeker.setLocation(profileDTO.getLocation());
        jobSeeker.setSummary(profileDTO.getSummary());
        jobSeeker.setSkills(profileDTO.getSkills());
        jobSeeker.setUpdatedAt(LocalDateTime.now());

        // Update experiences and educations if provided
        if (profileDTO.getExperiences() != null) {
            jobSeeker.getExperiences().clear();
            profileDTO.getExperiences().forEach(expDTO -> {
                jobSeeker.getExperiences().add(
                        com.hirable.entity.Experience.builder()
                                .company(expDTO.getCompany())
                                .title(expDTO.getTitle())
                                .startDate(expDTO.getStartDate())
                                .endDate(expDTO.getEndDate())
                                .description(expDTO.getDescription())
                                .build()
                );
            });
        }

        if (profileDTO.getEducations() != null) {
            jobSeeker.getEducations().clear();
            profileDTO.getEducations().forEach(eduDTO -> {
                jobSeeker.getEducations().add(
                        com.hirable.entity.Education.builder()
                                .institution(eduDTO.getInstitution())
                                .degree(eduDTO.getDegree())
                                .fieldOfStudy(eduDTO.getFieldOfStudy())
                                .graduationDate(eduDTO.getGraduationDate())
                                .build()
                );
            });
        }

        JobSeeker updated = jobSeekerRepository.save(jobSeeker);
        return mapToDTO(updated);
    }

    public String uploadResume(Long jobSeekerId, MultipartFile file) throws IOException {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new UserNotFoundException(jobSeekerId));

        // Delete old resume if exists
        if (jobSeeker.getResumeFilePath() != null) {
            resumeService.deleteResumeFile(jobSeeker.getResumeFilePath());
        }

        // Save new resume file
        String filename = resumeService.saveResumeFile(file);

        // Parse resume and update profile
        ParsedResumeData parsedData = resumeService.parseResume(file);
        jobSeeker.setResumeFilePath(filename);
        jobSeeker.setResumeUploadedAt(LocalDateTime.now());
        jobSeeker.setSkills(parsedData.getSkills());

        // Update experiences
        jobSeeker.getExperiences().clear();
        parsedData.getExperiences().forEach(expDTO -> {
            jobSeeker.getExperiences().add(
                    com.hirable.entity.Experience.builder()
                            .company(expDTO.getCompany())
                            .title(expDTO.getTitle())
                            .startDate(expDTO.getStartDate())
                            .endDate(expDTO.getEndDate())
                            .description(expDTO.getDescription())
                            .build()
            );
        });

        // Update educations
        jobSeeker.getEducations().clear();
        parsedData.getEducations().forEach(eduDTO -> {
            jobSeeker.getEducations().add(
                    com.hirable.entity.Education.builder()
                            .institution(eduDTO.getInstitution())
                            .degree(eduDTO.getDegree())
                            .fieldOfStudy(eduDTO.getFieldOfStudy())
                            .graduationDate(eduDTO.getGraduationDate())
                            .build()
            );
        });

        // Update relevance score
        updateRelevanceScore(jobSeeker);

        jobSeekerRepository.save(jobSeeker);
        return filename;
    }

    public void updateRelevanceScore(JobSeeker jobSeeker) {
        if (jobSeeker.getResumeUploadedAt() == null) {
            jobSeeker.setRelevanceScore(0.0);
            return;
        }

        long daysSinceUpdate = ChronoUnit.DAYS.between(
                jobSeeker.getResumeUploadedAt(),
                LocalDateTime.now()
        );

        double recencyScore;
        if (daysSinceUpdate <= 7) {
            recencyScore = 1.0;
        } else if (daysSinceUpdate <= 30) {
            recencyScore = 0.8;
        } else if (daysSinceUpdate <= 90) {
            recencyScore = 0.6;
        } else {
            recencyScore = 0.4;
        }

        jobSeeker.setRelevanceScore(recencyScore);
    }

    public JobSeeker createJobSeekerProfile(User user) {
        JobSeeker jobSeeker = JobSeeker.builder()
                .user(user)
                .firstName("")
                .lastName("")
                .phone("")
                .location("")
                .summary("")
                .relevanceScore(0.0)
                .build();
        return jobSeekerRepository.save(jobSeeker);
    }

    private JobSeekerProfileDTO mapToDTO(JobSeeker jobSeeker) {
        return JobSeekerProfileDTO.builder()
                .id(jobSeeker.getId())
                .firstName(jobSeeker.getFirstName())
                .lastName(jobSeeker.getLastName())
                .phone(jobSeeker.getPhone())
                .location(jobSeeker.getLocation())
                .summary(jobSeeker.getSummary())
                .skills(jobSeeker.getSkills())
                .experiences(jobSeeker.getExperiences().stream()
                        .map(exp -> com.hirable.dto.ExperienceDTO.builder()
                                .id(exp.getId())
                                .company(exp.getCompany())
                                .title(exp.getTitle())
                                .startDate(exp.getStartDate())
                                .endDate(exp.getEndDate())
                                .description(exp.getDescription())
                                .build())
                        .toList())
                .educations(jobSeeker.getEducations().stream()
                        .map(edu -> com.hirable.dto.EducationDTO.builder()
                                .id(edu.getId())
                                .institution(edu.getInstitution())
                                .degree(edu.getDegree())
                                .fieldOfStudy(edu.getFieldOfStudy())
                                .graduationDate(edu.getGraduationDate())
                                .build())
                        .toList())
                .resumeFilePath(jobSeeker.getResumeFilePath())
                .resumeUploadedAt(jobSeeker.getResumeUploadedAt())
                .relevanceScore(jobSeeker.getRelevanceScore())
                .build();
    }
}
