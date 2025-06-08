package com.br.chat.core.usecase.chat;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserChatUseCaseTest {

    @Mock
    private ChatRepositoryPortOut chatRepositoryPortOut;

    @InjectMocks
    private ListUserChatUseCase listUserChatUseCase;

    @Test
    void shouldReturnUserChats() {
        var userId = UUID.randomUUID();

        var chat = new Chat();
        chat.setUsers(List.of());

        var chatList = List.of(chat);
        when(chatRepositoryPortOut.findAllByUserId(userId)).thenReturn(chatList);

        var result = listUserChatUseCase.execute(userId);
        assertThat(result).hasSize(1);
        verify(chatRepositoryPortOut).findAllByUserId(userId);
    }
}
