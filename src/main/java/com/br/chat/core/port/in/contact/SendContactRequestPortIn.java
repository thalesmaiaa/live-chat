package com.br.chat.core.port.in.contact;

import com.br.chat.core.domain.message.NotificationMessage;

import java.util.UUID;

public interface SendContactRequestPortIn {

    NotificationMessage execute(UUID userId, String email);

}
