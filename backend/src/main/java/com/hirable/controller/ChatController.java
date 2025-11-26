package com.hirable.controller;

import com.hirable.dto.ChatDTO;
import com.hirable.dto.MessageDTO;
import com.hirable.entity.Chat;
import com.hirable.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER')")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long userId) {
        List<ChatDTO> chats = chatService.getUserChats(userId);
        return ResponseEntity.ok(chats);
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ChatDTO> createChat(@RequestBody Map<String, Long> request) {
        Long recruiterId = request.get("recruiterId");
        Long jobSeekerId = request.get("jobSeekerId");
        ChatDTO chat = chatService.createChat(recruiterId, jobSeekerId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{chatId}/messages")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER')")
    public ResponseEntity<List<MessageDTO>> getChatMessages(@PathVariable Long chatId) {
        List<MessageDTO> messages = chatService.getChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatDTO>> getAllChats() {
        List<ChatDTO> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @PutMapping("/{chatId}/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> flagChat(@PathVariable Long chatId, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        Long adminId = Long.parseLong(request.get("adminId"));
        chatService.flagChat(chatId, reason);
        chatService.logChatFlag(chatId, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{chatId}/unflag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unflagChat(@PathVariable Long chatId, @RequestBody Map<String, Long> request) {
        Long adminId = request.get("adminId");
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setFlagged(false);
        chat.setFlagReason(null);
        chatService.logChatUnflag(chatId, adminId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/disable-chat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUserChat(@PathVariable Long userId, @RequestBody Map<String, Object> request) {
        Long adminId = ((Number) request.get("adminId")).longValue();
        String reason = (String) request.get("reason");
        chatService.disableUserChat(userId, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/enable-chat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUserChat(@PathVariable Long userId, @RequestBody Map<String, Long> request) {
        Long adminId = request.get("adminId");
        chatService.enableUserChat(userId, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{chatId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER')")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        ChatDTO chat = chatService.getChatById(chatId);
        return ResponseEntity.ok(chat);
    }
}
