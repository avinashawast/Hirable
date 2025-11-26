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
public class CandidateSearchResult {
    private Long candidateId;
    private String firstName;
    private String lastName;
    private List<String> skills;
    private int experience;
    private String location;
    private double relevanceScore;
    private LocalDateTime lastResumeUpdate;
    private String resumeFilePath;
}
