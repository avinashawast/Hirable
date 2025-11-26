package com.hirable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDTO {
    private long totalJobSeekers;
    private long totalRecruiters;
    private long totalAdmins;
    private long totalJobs;
    private long approvedJobs;
    private long pendingJobs;
    private long rejectedJobs;
    private long totalApplications;
    private long totalChats;
    private long totalMessages;
    private long totalNotifications;
    private RecruiterActivityDTO recruiterActivity;
    private JobSeekerActivityDTO jobSeekerActivity;
    private ChatActivityDTO chatActivity;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruiterActivityDTO {
        private long totalJobsPosted;
        private long jobsApproved;
        private long jobsRejected;
        private long candidatesContacted;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobSeekerActivityDTO {
        private long totalApplicationsSubmitted;
        private long profilesWithResumes;
        private long averageApplicationsPerSeeker;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatActivityDTO {
        private long totalChats;
        private long totalMessages;
        private long averageMessagesPerChat;
    }
}
