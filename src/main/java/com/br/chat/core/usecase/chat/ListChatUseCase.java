package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.responses.ChatResponse;
import com.br.chat.core.exception.ChatNotFoundException;
import com.br.chat.core.port.in.chat.ListChatPortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListChatUseCase implements ListChatPortIn {

    private final ChatRepositoryPortOut chatRepositoryPortOut;

    @Override
    public ChatResponse execute(UUID chatId) {
        var chat = chatRepositoryPortOut.findChatById(chatId).orElseThrow(ChatNotFoundException::new);
        return chat.toChatResponse();
    }
}
