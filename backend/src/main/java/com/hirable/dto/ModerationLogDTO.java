package com.hirable.dto;

import com.hirable.entity.ModerationLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationLogDTO {
    private Long id;
    private String adminName;
    private String targetName;
    private Long chatId;
    private Long userId;
    private String action;
    private String reason;
    private LocalDateTime createdAt;
}
