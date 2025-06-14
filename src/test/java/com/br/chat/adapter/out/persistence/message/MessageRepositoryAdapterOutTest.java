package com.br.chat.adapter.out.persistence.message;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageRepositoryAdapterOutTest {

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageRepositoryAdapterOut adapter;

    @Test
    void shouldSaveMessageEntityWhenSaveChatMessage() {
        var message = mock(Message.class);
        var chat = mock(Chat.class);
        var user = mock(User.class);

        when(user.getId()).thenReturn(UUID.randomUUID());
        when(chat.getType()).thenReturn(ChatType.PRIVATE);
        when(message.getSenderUser()).thenReturn(user);
        
        adapter.saveChatMessage(message, chat);
        verify(messageRepository).save(any(MessageEntity.class));
    }
}
