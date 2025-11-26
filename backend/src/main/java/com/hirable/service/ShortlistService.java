package com.hirable.service;

import com.hirable.dto.ShortlistDTO;
import com.hirable.entity.JobSeeker;
import com.hirable.entity.Recruiter;
import com.hirable.entity.Shortlist;
import com.hirable.repository.JobSeekerRepository;
import com.hirable.repository.RecruiterRepository;
import com.hirable.repository.ShortlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortlistService {
    @Autowired
    private ShortlistRepository shortlistRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private NotificationService notificationService;

    public ShortlistDTO addToShortlist(Long recruiterId, Long jobSeekerId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        // Check if already shortlisted
        if (shortlistRepository.findByRecruiterIdAndJobSeekerId(recruiterId, jobSeekerId).isPresent()) {
            throw new RuntimeException("Candidate already in shortlist");
        }

        Shortlist shortlist = Shortlist.builder()
                .recruiter(recruiter)
                .jobSeeker(jobSeeker)
                .addedAt(LocalDateTime.now())
                .build();

        Shortlist saved = shortlistRepository.save(shortlist);
        
        // Create notification for job seeker
        notificationService.createNotification(
                jobSeeker.getUser().getId(),
                "RECRUITER_INTEREST",
                recruiter.getFirstName() + " " + recruiter.getLastName() + " from " + recruiter.getCompanyName() + " is interested in you"
        );
        
        return mapToDTO(saved);
    }

    public void removeFromShortlist(Long recruiterId, Long jobSeekerId) {
        shortlistRepository.deleteByRecruiterIdAndJobSeekerId(recruiterId, jobSeekerId);
    }

    public List<ShortlistDTO> getShortlist(Long recruiterId) {
        List<Shortlist> shortlists = shortlistRepository.findByRecruiterId(recruiterId);
        return shortlists.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean isShortlisted(Long recruiterId, Long jobSeekerId) {
        return shortlistRepository.findByRecruiterIdAndJobSeekerId(recruiterId, jobSeekerId).isPresent();
    }

    private ShortlistDTO mapToDTO(Shortlist shortlist) {
        JobSeeker jobSeeker = shortlist.getJobSeeker();
        return ShortlistDTO.builder()
                .id(shortlist.getId())
                .recruiterId(shortlist.getRecruiter().getId())
                .jobSeekerId(jobSeeker.getId())
                .firstName(jobSeeker.getFirstName())
                .lastName(jobSeeker.getLastName())
                .location(jobSeeker.getLocation())
                .summary(jobSeeker.getSummary())
                .skills(jobSeeker.getSkills())
                .addedAt(shortlist.getAddedAt())
                .build();
    }
}
