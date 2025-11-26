package com.hirable.service;

import com.hirable.dto.RecruiterProfileDTO;
import com.hirable.dto.ShortlistDTO;
import com.hirable.entity.Recruiter;
import com.hirable.entity.User;
import com.hirable.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private ShortlistService shortlistService;

    public RecruiterProfileDTO getProfile(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return mapToDTO(recruiter);
    }

    public RecruiterProfileDTO updateProfile(Long recruiterId, RecruiterProfileDTO profileDTO) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        recruiter.setFirstName(profileDTO.getFirstName());
        recruiter.setLastName(profileDTO.getLastName());
        recruiter.setPhone(profileDTO.getPhone());
        recruiter.setCompanyName(profileDTO.getCompanyName());
        recruiter.setCompanyWebsite(profileDTO.getCompanyWebsite());
        recruiter.setCompanyDescription(profileDTO.getCompanyDescription());
        recruiter.setIndustry(profileDTO.getIndustry());
        recruiter.setUpdatedAt(LocalDateTime.now());

        Recruiter updated = recruiterRepository.save(recruiter);
        return mapToDTO(updated);
    }

    public Recruiter createRecruiterProfile(User user) {
        Recruiter recruiter = Recruiter.builder()
                .user(user)
                .firstName("")
                .lastName("")
                .phone("")
                .companyName("")
                .companyWebsite("")
                .companyDescription("")
                .industry("")
                .build();
        return recruiterRepository.save(recruiter);
    }

    public List<ShortlistDTO> getShortlist(Long recruiterId) {
        return shortlistService.getShortlist(recruiterId);
    }

    public ShortlistDTO addToShortlist(Long recruiterId, Long jobSeekerId) {
        return shortlistService.addToShortlist(recruiterId, jobSeekerId);
    }

    public void removeFromShortlist(Long recruiterId, Long jobSeekerId) {
        shortlistService.removeFromShortlist(recruiterId, jobSeekerId);
    }

    public boolean isShortlisted(Long recruiterId, Long jobSeekerId) {
        return shortlistService.isShortlisted(recruiterId, jobSeekerId);
    }

    private RecruiterProfileDTO mapToDTO(Recruiter recruiter) {
        return RecruiterProfileDTO.builder()
                .id(recruiter.getId())
                .firstName(recruiter.getFirstName())
                .lastName(recruiter.getLastName())
                .phone(recruiter.getPhone())
                .companyName(recruiter.getCompanyName())
                .companyWebsite(recruiter.getCompanyWebsite())
                .companyDescription(recruiter.getCompanyDescription())
                .industry(recruiter.getIndustry())
                .build();
    }
}
