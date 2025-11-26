package com.hirable.service;

import com.hirable.dto.ApplicationDTO;
import com.hirable.entity.Application;
import com.hirable.entity.ApplicationStatus;
import com.hirable.entity.Job;
import com.hirable.entity.JobSeeker;
import com.hirable.repository.ApplicationRepository;
import com.hirable.repository.JobRepository;
import com.hirable.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private NotificationService notificationService;

    public ApplicationDTO createApplication(Long jobSeekerId, Long jobId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if already applied
        if (applicationRepository.findByJobSeekerIdAndJobId(jobSeekerId, jobId).isPresent()) {
            throw new RuntimeException("Already applied to this job");
        }

        Application application = Application.builder()
                .jobSeeker(jobSeeker)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .build();

        Application saved = applicationRepository.save(application);
        
        // Create notification for recruiter
        notificationService.createNotification(
                job.getRecruiter().getUser().getId(),
                "JOB_APPLICATION",
                jobSeeker.getFirstName() + " " + jobSeeker.getLastName() + " applied to " + job.getTitle()
        );
        
        return mapToDTO(saved);
    }

    public List<ApplicationDTO> getApplicationsByJobSeeker(Long jobSeekerId) {
        List<Application> applications = applicationRepository.findByJobSeekerId(jobSeekerId);
        return applications.stream().map(this::mapToDTO).toList();
    }

    private ApplicationDTO mapToDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .jobSeekerId(application.getJobSeeker().getId())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getRecruiter().getCompanyName())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .build();
    }
}
