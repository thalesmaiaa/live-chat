package com.br.chat.core.usecase.message;

import com.br.chat.core.domain.message.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationUseCaseTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationUseCase notificationUseCase;

    @Test
    void shouldSendNotification() {
        var message = new NotificationMessage();
        message.setDestination("/topic/test");
        notificationUseCase.execute(message);
        verify(messagingTemplate).convertAndSend(message.getDestination(), message);
    }
}
