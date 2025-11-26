package com.hirable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResumeData {
    private List<String> skills;
    private List<ExperienceDTO> experiences;
    private List<EducationDTO> educations;
}
