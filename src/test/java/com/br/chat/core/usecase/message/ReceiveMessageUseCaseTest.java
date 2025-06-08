package com.br.chat.core.usecase.message;

import com.br.chat.adapter.out.events.NotificationEventPublisher;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.MessageRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ReceiveMessageUseCaseTest {

    private ChatRepositoryPortOut chatRepositoryPortOut;
    private UserRepositoryPortOut userRepositoryPortOut;
    private MessageRepositoryPortOut messageRepositoryPortOut;
    private NotificationEventPublisher notificationEventPublisher;
    private ReceiveMessageUseCase receiveMessageUseCase;

    @BeforeEach
    void setUp() {
        chatRepositoryPortOut = mock(ChatRepositoryPortOut.class);
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        messageRepositoryPortOut = mock(MessageRepositoryPortOut.class);
        notificationEventPublisher = mock(NotificationEventPublisher.class);
        receiveMessageUseCase = new ReceiveMessageUseCase(
                chatRepositoryPortOut, userRepositoryPortOut, messageRepositoryPortOut, notificationEventPublisher
        );
    }

    @Test
    void shouldStartPrivateChat() {
        ChatMessage chatMessage = new ChatMessage(null, UUID.randomUUID(), UUID.randomUUID(), ChatType.PRIVATE, "msg", ZonedDateTime.now());
        User sender = new User(UUID.randomUUID(), "email", "username");
        chatMessage.setSenderUser(sender);

        when(userRepositoryPortOut.findById(any())).thenReturn(Optional.of(sender));
        when(userRepositoryPortOut.findById(chatMessage.getReceiverId())).thenReturn(Optional.of(new User(UUID.randomUUID(), "other", "other")));

        receiveMessageUseCase.execute(chatMessage);
        verify(chatRepositoryPortOut).startChat(any());
        verify(notificationEventPublisher).publishEvent(any());
    }

    @Test
    void shouldUpdateChatMessages() {
        UUID chatId = UUID.randomUUID();
        User sender = new User(UUID.randomUUID(), "email", "username");
        ChatMessage chatMessage = new ChatMessage(chatId, sender.getId(), UUID.randomUUID(), ChatType.GROUP, "msg", ZonedDateTime.now());
        chatMessage.setSenderUser(sender);

        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setUsers(List.of(sender));
        when(chatRepositoryPortOut.findChatById(chatId)).thenReturn(Optional.of(chat));

        receiveMessageUseCase.execute(chatMessage);
        verify(messageRepositoryPortOut).saveChatMessage(any(Message.class), eq(chat));
        verify(notificationEventPublisher).publishAllEvents(anyList());
    }
}
