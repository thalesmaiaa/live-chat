package com.br.chat.core.domain.events;

import com.br.chat.core.domain.message.NotificationMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private final NotificationMessage notificationMessage;

    public NotificationEvent(Object source, NotificationMessage notificationMessage) {
        super(source);
        this.notificationMessage = notificationMessage;
    }

}
