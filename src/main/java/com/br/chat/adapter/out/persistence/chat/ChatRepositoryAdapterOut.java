package com.br.chat.adapter.out.persistence.chat;

import com.br.chat.adapter.out.persistence.message.MessageEntity;
import com.br.chat.adapter.out.persistence.message.MessageRepository;
import com.br.chat.adapter.out.persistence.user.UserEntity;
import com.br.chat.adapter.out.persistence.userchat.UserChatEntity;
import com.br.chat.adapter.out.persistence.userchat.UserChatRepository;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRepositoryAdapterOut implements ChatRepositoryPortOut {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserChatRepository userChatRepository;

    @Override
    public UUID startChat(Chat chat) {
        var chatEntity = ChatEntity.fromDomain(chat);
        var savedChat = chatRepository.save(chatEntity);

        var usersChatEntities = chat.getUsers().stream()
                .map(user -> new UserChatEntity(UserEntity.fromDomain(user), savedChat)).toList();
        usersChatEntities.forEach(u -> u.setOwner(true));

        var messageEntities = chat.getMessages().stream().map(MessageEntity::fromDomain).toList();
        messageEntities.forEach(m -> m.setChat(savedChat));


        userChatRepository.saveAll(usersChatEntities);
        messageRepository.saveAll(messageEntities);

        return savedChat.getId();
    }


    @Override
    public Optional<Chat> findChatById(UUID id) {
        return chatRepository.findById(id).map(ChatEntity::toDomain);
    }

    @Override
    public void createGroupChat(Chat chat) {
        var chatEntity = ChatEntity.fromDomain(chat);
        var savedChat = chatRepository.save(chatEntity);

        var usersChatEntities = chat.getUsers().stream()
                .map(user -> {
                    var userChatEntity = new UserChatEntity(UserEntity.fromDomain(user), savedChat);
                    userChatEntity.setOwner(user.getId().equals(chat.getOwnerId()));
                    return userChatEntity;
                }).toList();

        userChatRepository.saveAll(usersChatEntities);
    }

    @Override
    public List<Chat> findAllByUserId(UUID userId) {
        return chatRepository.findAllByUserId(userId).stream()
                .map(ChatEntity::toDomain)
                .toList();
    }
}
