package com.br.chat.core.usecase.message;

import com.br.chat.core.domain.message.NotificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class NotificationUseCaseTest {

    private SimpMessagingTemplate messagingTemplate;
    private NotificationUseCase notificationUseCase;

    @BeforeEach
    void setUp() {
        messagingTemplate = mock(SimpMessagingTemplate.class);
        notificationUseCase = new NotificationUseCase(messagingTemplate);
    }

    @Test
    void shouldSendNotification() {
        NotificationMessage message = new NotificationMessage();
        message.setDestination("/topic/test");
        notificationUseCase.execute(message);
        verify(messagingTemplate).convertAndSend(message.getDestination(), message);
    }
}
