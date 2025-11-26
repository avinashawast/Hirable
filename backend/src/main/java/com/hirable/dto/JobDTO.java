package com.hirable.dto;

import com.hirable.entity.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private String location;
    private String experienceLevel;
    private String industry;
    private JobStatus status;
    private LocalDateTime postedAt;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private RecruiterDTO recruiter;
}
