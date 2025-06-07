package com.br.chat.adapter.out.persistence.chat;

import com.br.chat.adapter.out.persistence.message.MessageEntity;
import com.br.chat.adapter.out.persistence.userchat.UserChatEntity;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type;

    private String name;

    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "chat")
    private List<MessageEntity> messages;

    @OneToMany(mappedBy = "chat")
    private List<UserChatEntity> users;

    public static ChatEntity fromDomain(Chat chat) {
        var entity = new ChatEntity();
        entity.setId(chat.getId());
        entity.setType(chat.getType().name());
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setName(chat.getName());

        return entity;
    }

    public Chat toDomain() {
        var chat = new Chat();
        chat.setId(this.id);
        chat.setType(ChatType.valueOf(this.type));
        chat.setCreatedAt(this.createdAt);
        chat.setName(this.name);
        chat.setMessages(this.messages.stream().map(MessageEntity::toDomain).toList());
        chat.setUsers(this.users.stream().map(uc -> uc.getUser().toDomain()).toList());
        chat.setOwnerId(this.users.stream()
                .filter(UserChatEntity::isOwner)
                .findFirst()
                .map(uc -> uc.getUser().getId())
                .orElse(null));
        return chat;
    }
}
