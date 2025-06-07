package com.br.chat.core.port.in.chat;

import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;

import java.util.UUID;

public interface CreatePrivateChatPortIn {

    UUID execute(CreatePrivateChatRequest chat);
}
