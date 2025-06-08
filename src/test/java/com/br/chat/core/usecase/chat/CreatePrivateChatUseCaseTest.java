package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.NotificationPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePrivateChatUseCaseTest {

    @Mock
    private ChatRepositoryPortOut chatRepositoryPortOut;
    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;
    @Mock
    private NotificationPortOut notificationEventPublisher;

    @InjectMocks
    private CreatePrivateChatUseCase createPrivateChatUseCase;

    @Test
    void shouldCreatePrivateChat() {
        var senderId = UUID.randomUUID();
        var receiverId = UUID.randomUUID();
        var request = new CreatePrivateChatRequest(senderId, receiverId, "msg");
        var sender = new User(senderId, "sender@email.com", "sender");
        var receiver = new User(receiverId, "receiver@email.com", "receiver");

        when(userRepositoryPortOut.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepositoryPortOut.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(chatRepositoryPortOut.startChat(any())).thenReturn(UUID.randomUUID());

        var chatId = createPrivateChatUseCase.execute(request);
        assertThat(chatId).isNotNull();
        verify(notificationEventPublisher).publishEvent(any());
    }
}
