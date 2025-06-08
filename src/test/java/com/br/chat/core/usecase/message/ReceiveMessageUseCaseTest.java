package com.br.chat.core.usecase.message;

import com.br.chat.adapter.out.events.NotificationEventPublisher;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.domain.message.NotificationType;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.MessageRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveMessageUseCaseTest {

    @Mock
    private ChatRepositoryPortOut chatRepositoryPortOut;
    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;
    @Mock
    private MessageRepositoryPortOut messageRepositoryPortOut;
    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private ReceiveMessageUseCase receiveMessageUseCase;

    @Test
    void shouldStartPrivateChat() {
        var chatMessage = new ChatMessage(null, UUID.randomUUID(), UUID.randomUUID(), ChatType.PRIVATE, "msg", ZonedDateTime.now());
        var sender = new User(UUID.randomUUID(), "email", "username");
        chatMessage.setSenderUser(sender);

        when(userRepositoryPortOut.findById(any())).thenReturn(Optional.of(sender));
        when(userRepositoryPortOut.findById(chatMessage.getReceiverId())).thenReturn(Optional.of(new User(UUID.randomUUID(), "other", "other")));

        receiveMessageUseCase.execute(chatMessage);
        verify(chatRepositoryPortOut).startChat(any());
        verify(notificationEventPublisher).publishEvent(any());
    }

    @Test
    void shouldUpdateChatMessages() {
        var chatId = UUID.randomUUID();
        var sender = new User(UUID.randomUUID(), "email", "username");
        var chatMessage = new ChatMessage(chatId, sender.getId(), UUID.randomUUID(), ChatType.GROUP, "msg", ZonedDateTime.now());
        chatMessage.setSenderUser(sender);

        var chat = new Chat();
        chat.setId(chatId);
        chat.setUsers(List.of(sender));
        when(chatRepositoryPortOut.findChatById(chatId)).thenReturn(Optional.of(chat));

        receiveMessageUseCase.execute(chatMessage);
        verify(messageRepositoryPortOut).saveChatMessage(any(Message.class), eq(chat));
        verify(notificationEventPublisher).publishAllEvents(anyList());
    }

    @Test
    void shouldPublishNotificationsForAllChatMembersExceptSender() {
        var sender = new User(UUID.randomUUID(), "sender@email.com", "sender");
        var member1 = new User(UUID.randomUUID(), "member1@email.com", "member1");
        var member2 = new User(UUID.randomUUID(), "member2@email.com", "member2");
        var chatId = UUID.randomUUID();

        var chat = new Chat();
        chat.setId(chatId);
        chat.setName("Test Chat");
        chat.setUsers(List.of(sender, member1, member2));

        var chatMessage = new ChatMessage(chatId, sender.getId(), UUID.randomUUID(), ChatType.GROUP, "msg", ZonedDateTime.now());
        chatMessage.setSenderUser(sender);

        when(chatRepositoryPortOut.findChatById(chatId)).thenReturn(Optional.of(chat));

        ArgumentCaptor<List<NotificationMessage>> captor = ArgumentCaptor.forClass(List.class);

        receiveMessageUseCase.execute(chatMessage);

        verify(notificationEventPublisher).publishAllEvents(captor.capture());
        List<NotificationMessage> notifications = captor.getValue();

        assertThat(notifications).hasSize(3);

        long correctDestinationIdCount = notifications.stream()
                .filter(n -> chat.getId().equals(n.getDestinationId()))
                .count();
        assertThat(correctDestinationIdCount).isGreaterThanOrEqualTo(2);

        for (NotificationMessage notification : notifications) {
            assertThat(notification.getSenderUser().getId()).isEqualTo(sender.getId());
            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.NEW_MESSAGE);
            assertThat(notification.getDestination()).contains("/topics/");
            assertThat(notification.getSentAt()).isNotNull();
        }
    }
}
