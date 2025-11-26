package com.hirable.controller;

import com.hirable.dto.ChatMessageRequest;
import com.hirable.dto.MessageDTO;
import com.hirable.service.ChatService;
import com.hirable.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest message, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());
        
        // Save message to database
        MessageDTO savedMessage = chatService.sendMessage(message.getChatId(), senderId, message.getContent());
        
        // Send to recipient's queue
        messagingTemplate.convertAndSendToUser(
                message.getRecipientId().toString(),
                "/queue/messages",
                savedMessage
        );
        
        // Create notification for recipient
        notificationService.createNotification(
                message.getRecipientId(),
                "CHAT_MESSAGE",
                "New message from " + principal.getName()
        );
    }
}
