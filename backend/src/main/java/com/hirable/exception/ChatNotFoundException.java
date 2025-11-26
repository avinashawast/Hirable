package com.hirable.exception;

/**
 * Exception thrown when a chat is not found
 */
public class ChatNotFoundException extends HirableException {
    public ChatNotFoundException(String message) {
        super(message);
    }

    public ChatNotFoundException(Long chatId) {
        super("Chat with id " + chatId + " not found");
    }
}
