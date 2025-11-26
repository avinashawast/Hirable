package com.hirable.service;

import com.hirable.dto.ChatDTO;
import com.hirable.dto.MessageDTO;
import com.hirable.entity.*;
import com.hirable.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatService chatService;

    private User recruiterUser;
    private User jobSeekerUser;
    private Recruiter recruiter;
    private JobSeeker jobSeeker;
    private Chat chat;

    @BeforeEach
    void setUp() {
        recruiterUser = User.builder()
                .id(1L)
                .username("recruiter@test.com")
                .role(UserRole.RECRUITER)
                .email("recruiter@test.com")
                .active(true)
                .build();

        jobSeekerUser = User.builder()
                .id(2L)
                .username("jobseeker@test.com")
                .role(UserRole.JOB_SEEKER)
                .email("jobseeker@test.com")
                .active(true)
                .build();

        recruiter = Recruiter.builder()
                .id(1L)
                .user(recruiterUser)
                .firstName("John")
                .lastName("Recruiter")
                .companyName("Tech Corp")
                .build();

        jobSeeker = JobSeeker.builder()
                .id(1L)
                .user(jobSeekerUser)
                .firstName("Jane")
                .lastName("Seeker")
                .build();

        chat = Chat.builder()
                .id(1L)
                .recruiter(recruiter)
                .jobSeeker(jobSeeker)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateChat() {
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(jobSeekerRepository.findById(1L)).thenReturn(Optional.of(jobSeeker));
        when(chatRepository.findByRecruiterAndJobSeeker(1L, 1L)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        ChatDTO result = chatService.createChat(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane Seeker", result.getJobSeekerName());
        verify(chatRepository, times(1)).save(any(Chat.class));
    }

    @Test
    void testSendMessage() {
        Message message = Message.builder()
                .id(1L)
                .chat(chat)
                .sender(recruiterUser)
                .content("Hello")
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(userRepository.findById(1L)).thenReturn(Optional.of(recruiterUser));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageDTO result = chatService.sendMessage(1L, 1L, "Hello");

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        assertEquals(1L, result.getSenderId());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testFlagChat() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        chatService.flagChat(1L, "Inappropriate content");

        verify(chatRepository, times(1)).save(any(Chat.class));
    }
}
