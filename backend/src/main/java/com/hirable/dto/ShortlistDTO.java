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
public class ShortlistDTO {
    private Long id;
    private Long recruiterId;
    private Long jobSeekerId;
    private String firstName;
    private String lastName;
    private String location;
    private String summary;
    private java.util.List<String> skills;
    private LocalDateTime addedAt;
}
