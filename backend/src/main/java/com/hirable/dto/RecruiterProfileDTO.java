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
public class RecruiterProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String companyName;
    private String companyWebsite;
    private String companyDescription;
    private String industry;
}
