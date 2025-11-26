package com.hirable.service;

import com.hirable.dto.ChatDTO;
import com.hirable.dto.TalentPoolProfileDTO;
import com.hirable.entity.JobSeeker;
import com.hirable.repository.ChatRepository;
import com.hirable.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TalentPoolService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    public List<TalentPoolProfileDTO> getAllProfiles() {
        return jobSeekerRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<TalentPoolProfileDTO> getAllResumes() {
        return jobSeekerRepository.findAll().stream()
                .filter(js -> js.getResumeFilePath() != null)
                .map(this::mapToDTO)
                .toList();
    }

    public TalentPoolProfileDTO getProfileById(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));
        return mapToDTO(jobSeeker);
    }

    public void flagProfile(Long jobSeekerId, String reason) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));
        jobSeeker.setFlagged(true);
        jobSeeker.setFlagReason(reason);
        jobSeekerRepository.save(jobSeeker);
    }

    public void unflagProfile(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));
        jobSeeker.setFlagged(false);
        jobSeeker.setFlagReason(null);
        jobSeekerRepository.save(jobSeeker);
    }

    public void removeProfile(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));
        jobSeekerRepository.delete(jobSeeker);
    }

    public List<ChatDTO> getAllChats() {
        return chatService.getAllChats();
    }

    public void flagChat(Long chatId, String reason, Long adminId) {
        chatService.flagChat(chatId, reason);
        chatService.logChatFlag(chatId, adminId, reason);
    }

    public void unflagChat(Long chatId, Long adminId) {
        chatService.getChatById(chatId);
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.setFlagged(false);
            chat.setFlagReason(null);
            chatRepository.save(chat);
        });
        chatService.logChatUnflag(chatId, adminId);
    }

    public void disableUserChat(Long userId, Long adminId, String reason) {
        chatService.disableUserChat(userId, adminId, reason);
    }

    public void enableUserChat(Long userId, Long adminId) {
        chatService.enableUserChat(userId, adminId);
    }

    private TalentPoolProfileDTO mapToDTO(JobSeeker jobSeeker) {
        return TalentPoolProfileDTO.builder()
                .id(jobSeeker.getId())
                .firstName(jobSeeker.getFirstName())
                .lastName(jobSeeker.getLastName())
                .email(jobSeeker.getUser().getEmail())
                .phone(jobSeeker.getPhone())
                .location(jobSeeker.getLocation())
                .summary(jobSeeker.getSummary())
                .skills(jobSeeker.getSkills())
                .resumeFilePath(jobSeeker.getResumeFilePath())
                .resumeUploadedAt(jobSeeker.getResumeUploadedAt())
                .relevanceScore(jobSeeker.getRelevanceScore())
                .flagged(jobSeeker.isFlagged())
                .flagReason(jobSeeker.getFlagReason())
                .createdAt(jobSeeker.getCreatedAt())
                .updatedAt(jobSeeker.getUpdatedAt())
                .build();
    }
}
