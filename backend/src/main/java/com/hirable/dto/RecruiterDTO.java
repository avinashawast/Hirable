package com.hirable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String companyName;
    private String phone;
    private String industry;
}
