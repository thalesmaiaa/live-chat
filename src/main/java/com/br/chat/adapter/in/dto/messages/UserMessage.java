package com.br.chat.adapter.in.dto.messages;

import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.domain.chat.ChatType;

import java.time.ZonedDateTime;
import java.util.UUID;

public record UserMessage(UUID chatId, UUID receiverId, ChatType chatType, String message) {

    public ChatMessage toDomain(UUID senderId) {
        return new ChatMessage(chatId, senderId, receiverId, chatType, message, ZonedDateTime.now());
    }
}