package com.br.chat.core.port.out;

import com.br.chat.core.domain.message.NotificationMessage;

import java.util.List;

public interface NotificationPortOut {

    void publishEvent(NotificationMessage notificationMessage);

    void publishAllEvents(List<NotificationMessage> notificationMessages);
}
