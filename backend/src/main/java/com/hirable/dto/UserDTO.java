package com.hirable.dto;

import com.hirable.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
