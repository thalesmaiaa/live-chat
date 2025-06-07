package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.chat.CreateGroupChatPortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CreateGroupChatUseCase implements CreateGroupChatPortIn {

    private final ChatRepositoryPortOut chatRepositoryPortOut;
    private final UserRepositoryPortOut userRepositoryPortOut;

    @Override
    public void execute(CreateChatGroupRequest request) {
        var owner = userRepositoryPortOut.findById(request.ownerId()).orElseThrow(UserNotFoundException::new);
        var members = new ArrayList<>(userRepositoryPortOut.findAllById(request.members()));
        var isMissingMembers = members.size() != request.members().size();
        if (isMissingMembers) {
            throw new UserNotFoundException();
        }

        members.add(owner);
        var chat = Chat.toGroupChat(request.chatName(), owner.getId(), members);
        chatRepositoryPortOut.createGroupChat(chat);
    }

}
