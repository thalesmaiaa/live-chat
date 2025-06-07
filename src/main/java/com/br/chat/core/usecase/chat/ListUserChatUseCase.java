package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.responses.ChatResponse;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.port.in.chat.ListUserChatPortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListUserChatUseCase implements ListUserChatPortIn {

    private final ChatRepositoryPortOut chatRepositoryPortOut;

    @Override
    public List<ChatResponse> execute(UUID userId) {
        var chats = chatRepositoryPortOut.findAllByUserId(userId);
        return chats.stream().map(Chat::toChatResponse).toList();
    }
}
