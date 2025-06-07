package com.br.chat.core.port.in.chat;

import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;

public interface CreateGroupChatPortIn {

    void execute(CreateChatGroupRequest chat);
}
