package com.br.chat.adapter.in.dto.responses;

import java.time.ZonedDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String email, String username, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
}