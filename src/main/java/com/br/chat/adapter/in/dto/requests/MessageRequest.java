package com.br.chat.adapter.in.dto.requests;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessageRequest(@NotBlank String content, @NotBlank UUID senderId, @NotBlank UUID chatId,
                             @NotBlank String sentAt) {
}
