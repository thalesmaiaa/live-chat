package com.br.chat.core.port.out;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.message.Message;

public interface MessageRepositoryPortOut {

    void saveChatMessage(Message message, Chat chat);


}
