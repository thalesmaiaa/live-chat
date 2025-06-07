package com.br.chat.core.port.out;

import com.br.chat.core.domain.chat.Chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepositoryPortOut {

    Optional<Chat> findChatById(UUID id);

    void createGroupChat(Chat chat);

    List<Chat> findAllByUserId(UUID userId);

    UUID startChat(Chat chat);
}