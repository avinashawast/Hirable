package com.hirable.dto;

import com.hirable.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private Long id;
    private Long jobSeekerId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}
