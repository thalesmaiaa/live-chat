package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;
import com.br.chat.adapter.out.events.NotificationEventPublisher;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreatePrivateChatUseCaseTest {

    private ChatRepositoryPortOut chatRepositoryPortOut;
    private UserRepositoryPortOut userRepositoryPortOut;
    private NotificationEventPublisher notificationEventPublisher;
    private CreatePrivateChatUseCase createPrivateChatUseCase;

    @BeforeEach
    void setUp() {
        chatRepositoryPortOut = mock(ChatRepositoryPortOut.class);
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        notificationEventPublisher = mock(NotificationEventPublisher.class);
        createPrivateChatUseCase = new CreatePrivateChatUseCase(chatRepositoryPortOut, userRepositoryPortOut, notificationEventPublisher);
    }

    @Test
    void shouldCreatePrivateChat() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        CreatePrivateChatRequest request = new CreatePrivateChatRequest(senderId, receiverId, "msg");
        User sender = new User(senderId, "sender@email.com", "sender");
        User receiver = new User(receiverId, "receiver@email.com", "receiver");

        when(userRepositoryPortOut.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepositoryPortOut.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(chatRepositoryPortOut.startChat(any())).thenReturn(UUID.randomUUID());

        UUID chatId = createPrivateChatUseCase.execute(request);
        assertThat(chatId).isNotNull();
        verify(notificationEventPublisher).publishEvent(any());
    }
}
