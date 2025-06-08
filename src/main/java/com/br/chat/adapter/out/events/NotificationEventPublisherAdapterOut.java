package com.br.chat.adapter.out.events;

import com.br.chat.core.domain.events.NotificationEvent;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.port.out.NotificationPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisherAdapterOut implements NotificationPortOut {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(NotificationMessage message) {
        var customSpringEvent = new NotificationEvent(this, message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishAllEvents(List<NotificationMessage> messages) {
        messages.forEach(this::publishEvent);
    }
}