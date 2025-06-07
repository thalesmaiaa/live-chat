package com.br.chat.adapter.in.dto.responses;

import java.util.UUID;

public record ContactResponse(Long id, UserResponse user, Boolean hasActiveChat, UUID chatId) {
}
