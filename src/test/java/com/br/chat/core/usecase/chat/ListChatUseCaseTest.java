package com.br.chat.core.usecase.chat;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.exception.ChatNotFoundException;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListChatUseCaseTest {

    private ChatRepositoryPortOut chatRepositoryPortOut;
    private ListChatUseCase listChatUseCase;

    @BeforeEach
    void setUp() {
        chatRepositoryPortOut = mock(ChatRepositoryPortOut.class);
        listChatUseCase = new ListChatUseCase(chatRepositoryPortOut);
    }

    @Test
    void shouldReturnChatById() {
        var chatId = UUID.randomUUID();
        var chat = new Chat();
        chat.setUsers(List.of());
        when(chatRepositoryPortOut.findChatById(chatId)).thenReturn(Optional.of(chat));

        assertThat(listChatUseCase.execute(chatId)).isNotNull();
    }

    @Test
    void shouldThrowWhenChatNotFound() {
        UUID chatId = UUID.randomUUID();
        when(chatRepositoryPortOut.findChatById(chatId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listChatUseCase.execute(chatId))
            .isInstanceOf(ChatNotFoundException.class);
    }
}
