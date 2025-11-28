package com.hirable.service;

import com.hirable.dto.AnalyticsDTO;
import com.hirable.entity.JobStatus;
import com.hirable.entity.UserRole;
import com.hirable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
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
    private NotificationRepository notificationRepository;

    public AnalyticsDTO getAnalytics() {
        long totalJobSeekers = userRepository.findByRole(UserRole.JOB_SEEKER).size();
        long totalRecruiters = userRepository.findByRole(UserRole.RECRUITER).size();
        long totalAdmins = userRepository.findByRole(UserRole.ADMIN).size();

        long totalJobs = jobRepository.count();
        long approvedJobs = jobRepository.findByStatus(JobStatus.APPROVED.name()).size();
        long pendingJobs = jobRepository.findByStatus(JobStatus.PENDING.name()).size();
        long rejectedJobs = jobRepository.findByStatus(JobStatus.REJECTED.name()).size();

        long totalApplications = applicationRepository.count();
        long totalChats = chatRepository.count();
        long totalMessages = messageRepository.count();
        long totalNotifications = notificationRepository.count();

        AnalyticsDTO.RecruiterActivityDTO recruiterActivity = getRecruiterActivity();
        AnalyticsDTO.JobSeekerActivityDTO jobSeekerActivity = getJobSeekerActivity();
        AnalyticsDTO.ChatActivityDTO chatActivity = getChatActivity();

        return AnalyticsDTO.builder()
                .totalJobSeekers(totalJobSeekers)
                .totalRecruiters(totalRecruiters)
                .totalAdmins(totalAdmins)
                .totalJobs(totalJobs)
                .approvedJobs(approvedJobs)
                .pendingJobs(pendingJobs)
                .rejectedJobs(rejectedJobs)
                .totalApplications(totalApplications)
                .totalChats(totalChats)
                .totalMessages(totalMessages)
                .totalNotifications(totalNotifications)
                .recruiterActivity(recruiterActivity)
                .jobSeekerActivity(jobSeekerActivity)
                .chatActivity(chatActivity)
                .build();
    }

    private AnalyticsDTO.RecruiterActivityDTO getRecruiterActivity() {
        long totalJobsPosted = jobRepository.count();
        long jobsApproved = jobRepository.findByStatus(JobStatus.APPROVED.name()).size();
        long jobsRejected = jobRepository.findByStatus(JobStatus.REJECTED.name()).size();
        long candidatesContacted = chatRepository.count();

        return AnalyticsDTO.RecruiterActivityDTO.builder()
                .totalJobsPosted(totalJobsPosted)
                .jobsApproved(jobsApproved)
                .jobsRejected(jobsRejected)
                .candidatesContacted(candidatesContacted)
                .build();
    }

    private AnalyticsDTO.JobSeekerActivityDTO getJobSeekerActivity() {
        long totalApplicationsSubmitted = applicationRepository.count();
        long profilesWithResumes = userRepository.findByRole(UserRole.JOB_SEEKER).size();
        long averageApplicationsPerSeeker = totalApplicationsSubmitted > 0 ? 
                totalApplicationsSubmitted / Math.max(profilesWithResumes, 1) : 0;

        return AnalyticsDTO.JobSeekerActivityDTO.builder()
                .totalApplicationsSubmitted(totalApplicationsSubmitted)
                .profilesWithResumes(profilesWithResumes)
                .averageApplicationsPerSeeker(averageApplicationsPerSeeker)
                .build();
    }

    private AnalyticsDTO.ChatActivityDTO getChatActivity() {
        long totalChats = chatRepository.count();
        long totalMessages = messageRepository.count();
        long averageMessagesPerChat = totalChats > 0 ? totalMessages / totalChats : 0;

        return AnalyticsDTO.ChatActivityDTO.builder()
                .totalChats(totalChats)
                .totalMessages(totalMessages)
                .averageMessagesPerChat(averageMessagesPerChat)
                .build();
    }
}
