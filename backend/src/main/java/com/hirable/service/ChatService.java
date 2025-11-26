package com.hirable.service;

import com.hirable.dto.ChatDTO;
import com.hirable.dto.MessageDTO;
import com.hirable.entity.*;
import com.hirable.exception.ChatNotFoundException;
import com.hirable.exception.UnauthorizedException;
import com.hirable.exception.UserNotFoundException;
import com.hirable.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final ModerationLogRepository moderationLogRepository;

    public ChatDTO createChat(Long recruiterId, Long jobSeekerId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new UserNotFoundException(recruiterId));
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new UserNotFoundException(jobSeekerId));

        // Check if chat already exists
        var existingChat = chatRepository.findByRecruiterAndJobSeeker(recruiterId, jobSeekerId);
        if (existingChat.isPresent()) {
            return mapToDTO(existingChat.get());
        }

        Chat chat = Chat.builder()
                .recruiter(recruiter)
                .jobSeeker(jobSeeker)
                .createdAt(LocalDateTime.now())
                .build();

        Chat savedChat = chatRepository.save(chat);
        return mapToDTO(savedChat);
    }

    public MessageDTO sendMessage(Long chatId, Long senderId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(senderId));

        if (sender.isChatDisabled()) {
            throw new UnauthorizedException("User chat has been disabled by admin");
        }

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(content)
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();

        Message savedMessage = messageRepository.save(message);
        return mapMessageToDTO(savedMessage);
    }

    public List<MessageDTO> getChatMessages(Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        return messages.stream()
                .map(this::mapMessageToDTO)
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getUserChats(Long userId) {
        List<Chat> chats = chatRepository.findByUserId(userId);
        return chats.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getAllChats() {
        List<Chat> chats = chatRepository.findAllChats();
        return chats.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void flagChat(Long chatId, String reason) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        chat.setFlagged(true);
        chat.setFlagReason(reason);
        chatRepository.save(chat);
    }

    public ChatDTO getChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        return mapToDTO(chat);
    }

    public void disableUserChat(Long userId, Long adminId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        user.setChatDisabled(true);
        userRepository.save(user);

        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .targetUser(user)
                .action(ModerationLog.ModerationAction.DISABLE_USER_CHAT)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();
        moderationLogRepository.save(log);
    }

    public void enableUserChat(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        user.setChatDisabled(false);
        userRepository.save(user);

        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .targetUser(user)
                .action(ModerationLog.ModerationAction.ENABLE_USER_CHAT)
                .createdAt(LocalDateTime.now())
                .build();
        moderationLogRepository.save(log);
    }

    public void logChatFlag(Long chatId, Long adminId, String reason) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .chat(chat)
                .action(ModerationLog.ModerationAction.FLAG_CHAT)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();
        moderationLogRepository.save(log);
    }

    public void logChatUnflag(Long chatId, Long adminId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .chat(chat)
                .action(ModerationLog.ModerationAction.UNFLAG_CHAT)
                .createdAt(LocalDateTime.now())
                .build();
        moderationLogRepository.save(log);
    }

    private ChatDTO mapToDTO(Chat chat) {
        String recruiterName = chat.getRecruiter().getFirstName() + " " + chat.getRecruiter().getLastName();
        String jobSeekerName = chat.getJobSeeker().getFirstName() + " " + chat.getJobSeeker().getLastName();
        int messageCount = messageRepository.findByChatId(chat.getId()).size();

        return ChatDTO.builder()
                .id(chat.getId())
                .recruiterId(chat.getRecruiter().getId())
                .jobSeekerId(chat.getJobSeeker().getId())
                .recruiterName(recruiterName)
                .jobSeekerName(jobSeekerName)
                .createdAt(chat.getCreatedAt())
                .flagged(chat.isFlagged())
                .flagReason(chat.getFlagReason())
                .messageCount(messageCount)
                .build();
    }

    private MessageDTO mapMessageToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getUsername())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .read(message.isRead())
                .build();
    }
}
