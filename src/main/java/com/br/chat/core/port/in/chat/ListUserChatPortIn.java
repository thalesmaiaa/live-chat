package com.br.chat.core.port.in.chat;

import com.br.chat.adapter.in.dto.responses.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface ListUserChatPortIn {

    List<ChatResponse> execute(UUID userId);

}
