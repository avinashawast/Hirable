package com.hirable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long id;
    private Long recruiterId;
    private Long jobSeekerId;
    private String recruiterName;
    private String jobSeekerName;
    private LocalDateTime createdAt;
    private boolean flagged;
    private String flagReason;
    private int messageCount;
}
