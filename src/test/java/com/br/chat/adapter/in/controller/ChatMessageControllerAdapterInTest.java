package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.messages.UserMessage;
import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.port.in.message.ReceiveMessagePortIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageControllerAdapterInTest {

    @Mock
    private ReceiveMessagePortIn receiveMessagePortIn;

    @InjectMocks
    private ChatMessageControllerAdapterIn controller;

    @Test
    void shouldReceiveMessageCallPort() {
        var senderId = UUID.randomUUID();
        var userMessage = mock(UserMessage.class);
        var domainMessage = mock(ChatMessage.class);
        when(userMessage.toDomain(senderId)).thenReturn(domainMessage);
        controller.receiveMessage(senderId, userMessage);
        verify(receiveMessagePortIn).execute(domainMessage);
    }
}
