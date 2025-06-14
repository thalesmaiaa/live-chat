package com.br.chat.adapter.in.listener;

import com.br.chat.core.domain.events.NotificationEvent;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.port.in.message.NotificationUserPortIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationEventListenerAdapterInTest {

    @Mock
    private NotificationUserPortIn notificationUserPortIn;

    @InjectMocks
    private NotificationEventListenerAdapterIn listener;

    @Test
    void shouldOnApplicationEventCallPort() {
        var event = mock(NotificationEvent.class);
        var notificationMessage = mock(NotificationMessage.class);
        when(event.getNotificationMessage()).thenReturn(notificationMessage);
        listener.onApplicationEvent(event);
        verify(notificationUserPortIn).execute(notificationMessage);
    }
}
