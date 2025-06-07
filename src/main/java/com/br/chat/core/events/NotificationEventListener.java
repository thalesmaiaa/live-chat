package com.br.chat.core.events;

import com.br.chat.core.port.in.message.NotificationUserPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener implements ApplicationListener<NotificationEvent> {

    private final NotificationUserPortIn notificationUserPortIn;

    @Override
    public void onApplicationEvent(NotificationEvent event) {
        notificationUserPortIn.execute(event.getNotificationMessage());
    }
}
