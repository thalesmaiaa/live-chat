package com.br.chat.adapter.out.persistence.message;

import com.br.chat.adapter.out.persistence.chat.ChatEntity;
import com.br.chat.adapter.out.persistence.user.UserEntity;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.port.out.MessageRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapterOut implements MessageRepositoryPortOut {

    private final MessageRepository messageRepository;

    @Override
    public void saveChatMessage(Message message, Chat chat) {
        var newMessageEntity = MessageEntity.fromDomain(message);
        newMessageEntity.setChat(ChatEntity.fromDomain(chat));
        newMessageEntity.setUser(UserEntity.fromDomain(message.getSenderUser()));
        messageRepository.save(newMessageEntity);
    }
}
