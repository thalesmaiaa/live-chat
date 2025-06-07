package com.br.chat.core.port.in.message;

import com.br.chat.core.domain.chat.ChatMessage;

public interface ReceiveMessagePortIn {

    void execute(ChatMessage chat);
}
