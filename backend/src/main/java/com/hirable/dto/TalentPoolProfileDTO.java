package com.hirable.dto;

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
public class TalentPoolProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String location;
    private String summary;
    private List<String> skills;
    private String resumeFilePath;
    private LocalDateTime resumeUploadedAt;
    private double relevanceScore;
    private boolean flagged;
    private String flagReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
