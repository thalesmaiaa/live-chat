package com.br.chat.core.usecase.message;

import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.port.in.message.NotificationUserPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUseCase implements NotificationUserPortIn {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void execute(NotificationMessage notificationMessage) {
        messagingTemplate.convertAndSend(notificationMessage.getDestination(), notificationMessage);
    }
}
