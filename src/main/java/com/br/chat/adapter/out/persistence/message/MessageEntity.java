package com.br.chat.adapter.out.persistence.message;

import com.br.chat.adapter.out.persistence.chat.ChatEntity;
import com.br.chat.adapter.out.persistence.user.UserEntity;
import com.br.chat.core.domain.message.Message;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    private ZonedDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    public static MessageEntity fromDomain(Message message) {
        var entity = new MessageEntity();
        entity.setContent(message.getContent());
        entity.setUser(UserEntity.fromDomain(message.getSenderUser()));
        entity.setSentAt(message.getSentAt());
        return entity;
    }

    public Message toDomain() {
        var sender = this.user.toDomain();
        return new Message(sender, this.content, this.sentAt);
    }

}