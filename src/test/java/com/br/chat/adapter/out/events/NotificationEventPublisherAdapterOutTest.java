package com.br.chat.adapter.out.events;

import com.br.chat.core.domain.events.NotificationEvent;
import com.br.chat.core.domain.message.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class NotificationEventPublisherAdapterOutTest {
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    NotificationEventPublisherAdapterOut adapter;

    @Test
    void publishEvent_shouldPublishNotificationEvent() {
        var message = mock(NotificationMessage.class);
        assertThatCode(() -> adapter.publishEvent(message)).doesNotThrowAnyException();
        verify(applicationEventPublisher).publishEvent(any(NotificationEvent.class));
    }

    @Test
    void publishAllEvents_shouldPublishAll() {
        var message1 = mock(NotificationMessage.class);
        var message2 = mock(NotificationMessage.class);
        var messages = List.of(message1, message2);
        adapter.publishAllEvents(messages);
        verify(applicationEventPublisher, times(2)).publishEvent(any(NotificationEvent.class));
    }
}
