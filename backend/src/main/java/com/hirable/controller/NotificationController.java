package com.hirable.controller;

import com.hirable.dto.NotificationDTO;
import com.hirable.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/unread-count")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(@PathVariable Long userId) {
        int count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
}
