package com.br.chat.core.usecase.chat;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ListUserChatUseCaseTest {

    private ChatRepositoryPortOut chatRepositoryPortOut;
    private ListUserChatUseCase listUserChatUseCase;

    @BeforeEach
    void setUp() {
        chatRepositoryPortOut = mock(ChatRepositoryPortOut.class);
        listUserChatUseCase = new ListUserChatUseCase(chatRepositoryPortOut);
    }

    @Test
    void shouldReturnUserChats() {
        UUID userId = UUID.randomUUID();

        var chat = new Chat();
        chat.setUsers(List.of());

        when(chatRepositoryPortOut.findAllByUserId(userId)).thenReturn(List.of(chat));

        var result = listUserChatUseCase.execute(userId);
        assertThat(result).hasSize(1);
        verify(chatRepositoryPortOut).findAllByUserId(userId);
    }
}
