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
public class ExperienceDTO {
    private Long id;
    private String company;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
