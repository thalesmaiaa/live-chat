package com.br.chat.core.port.in.message;

import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.domain.message.NotificationMessage;

import java.util.List;

public interface ReceiveMessagePortIn {

    List<NotificationMessage> execute(ChatMessage chat);
}
