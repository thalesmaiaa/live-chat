package com.br.chat.core.port.in.chat;

import com.br.chat.adapter.in.dto.responses.ChatResponse;

import java.util.UUID;

public interface ListChatPortIn {

    ChatResponse execute(UUID chatId);
}
