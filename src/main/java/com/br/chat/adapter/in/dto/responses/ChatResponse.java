package com.br.chat.adapter.in.dto.responses;

import com.br.chat.core.domain.chat.ChatType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ChatResponse(UUID ownerId, UUID chatId, String name, ChatType chatType, List<UserResponse> members,
                           List<MessageResponse> messages, ZonedDateTime createdAt) {
}
