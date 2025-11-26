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
public class JobSeekerProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String summary;
    private List<String> skills;
    private List<ExperienceDTO> experiences;
    private List<EducationDTO> educations;
    private String resumeFilePath;
    private LocalDateTime resumeUploadedAt;
    private double relevanceScore;
}
