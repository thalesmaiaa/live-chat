package com.br.chat.adapter.in.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateChatGroupRequest(@NotNull UUID ownerId, @NotEmpty @Size(min = 1) List<UUID> members,
                                     String chatName) {
}