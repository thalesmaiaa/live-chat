package com.br.chat.core.port.in.message;

import com.br.chat.core.domain.message.NotificationMessage;

public interface NotificationUserPortIn {

    void execute(NotificationMessage message);
}