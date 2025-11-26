package com.hirable.service;

import com.hirable.dto.JobDTO;
import com.hirable.dto.RecruiterDTO;
import com.hirable.entity.Job;
import com.hirable.entity.JobStatus;
import com.hirable.entity.Recruiter;
import com.hirable.exception.JobNotFoundException;
import com.hirable.exception.UserNotFoundException;
import com.hirable.repository.JobRepository;
import com.hirable.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public JobDTO createJob(Long recruiterId, JobDTO jobDTO) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new UserNotFoundException(recruiterId));

        Job job = Job.builder()
                .recruiter(recruiter)
                .title(jobDTO.getTitle())
                .description(jobDTO.getDescription())
                .requiredSkills(jobDTO.getRequiredSkills())
                .location(jobDTO.getLocation())
                .experienceLevel(jobDTO.getExperienceLevel())
                .industry(jobDTO.getIndustry())
                .status(JobStatus.PENDING)
                .build();

        Job saved = jobRepository.save(job);
        return mapToDTO(saved);
    }

    public JobDTO updateJob(Long jobId, JobDTO jobDTO) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        job.setTitle(jobDTO.getTitle());
        job.setDescription(jobDTO.getDescription());
        job.setRequiredSkills(jobDTO.getRequiredSkills());
        job.setLocation(jobDTO.getLocation());
        job.setExperienceLevel(jobDTO.getExperienceLevel());
        job.setIndustry(jobDTO.getIndustry());
        job.setUpdatedAt(LocalDateTime.now());

        Job updated = jobRepository.save(job);
        return mapToDTO(updated);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
        jobRepository.delete(job);
    }

    public JobDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
        return mapToDTO(job);
    }

    public List<JobDTO> getJobsByRecruiterId(Long recruiterId) {
        List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);
        return jobs.stream().map(this::mapToDTO).toList();
    }

    public List<JobDTO> getPendingJobs() {
        List<Job> jobs = jobRepository.findByStatus(JobStatus.PENDING);
        return jobs.stream().map(this::mapToDTO).toList();
    }

    public JobDTO approveJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        job.setStatus(JobStatus.APPROVED);
        job.setApprovedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job updated = jobRepository.save(job);
        return mapToDTO(updated);
    }

    public JobDTO rejectJob(Long jobId, String reason) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        job.setStatus(JobStatus.REJECTED);
        job.setRejectionReason(reason);
        job.setUpdatedAt(LocalDateTime.now());

        Job updated = jobRepository.save(job);
        return mapToDTO(updated);
    }

    public void removeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        job.setStatus(JobStatus.REMOVED);
        job.setUpdatedAt(LocalDateTime.now());

        jobRepository.save(job);
    }

    public Page<JobDTO> searchJobs(String keyword, String location, String industry, 
                                    String experienceLevel, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Job> jobs = jobRepository.searchJobs(keyword, location, industry, experienceLevel, pageable);
        return jobs.map(this::mapToDTO);
    }

    public List<JobDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::mapToDTO).toList();
    }

    public List<JobDTO> getJobsByStatus(String status) {
        try {
            JobStatus jobStatus = JobStatus.valueOf(status.toUpperCase());
            List<Job> jobs = jobRepository.findByStatus(jobStatus);
            return jobs.stream().map(this::mapToDTO).toList();
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    private JobDTO mapToDTO(Job job) {
        RecruiterDTO recruiterDTO = null;
        if (job.getRecruiter() != null) {
            recruiterDTO = RecruiterDTO.builder()
                    .id(job.getRecruiter().getId())
                    .firstName(job.getRecruiter().getFirstName())
                    .lastName(job.getRecruiter().getLastName())
                    .companyName(job.getRecruiter().getCompanyName())
                    .phone(job.getRecruiter().getPhone())
                    .industry(job.getRecruiter().getIndustry())
                    .build();
        }

        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requiredSkills(job.getRequiredSkills())
                .location(job.getLocation())
                .experienceLevel(job.getExperienceLevel())
                .industry(job.getIndustry())
                .status(job.getStatus())
                .postedAt(job.getPostedAt())
                .approvedAt(job.getApprovedAt())
                .rejectionReason(job.getRejectionReason())
                .recruiter(recruiterDTO)
                .build();
    }
}
