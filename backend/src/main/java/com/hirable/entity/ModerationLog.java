package com.hirable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User targetUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModerationAction action;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ModerationAction {
        FLAG_CHAT,
        UNFLAG_CHAT,
        DISABLE_USER_CHAT,
        ENABLE_USER_CHAT
    }
}
