package com.br.chat.adapter.in.listener;

import com.br.chat.core.events.NotificationEvent;
import com.br.chat.core.port.in.message.NotificationUserPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListenerAdapterIn implements ApplicationListener<NotificationEvent> {

    private final NotificationUserPortIn notificationUserPortIn;

    @Override
    public void onApplicationEvent(NotificationEvent event) {
        notificationUserPortIn.execute(event.getNotificationMessage());
    }
}
