package com.hirable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDTO {
    private Long id;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private LocalDate graduationDate;
}
