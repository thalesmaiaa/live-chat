package com.br.chat.adapter.out.persistence.userchat;

import com.br.chat.adapter.out.persistence.chat.ChatEntity;
import com.br.chat.adapter.out.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users_chats")
public class UserChatEntity {

    @EmbeddedId
    private UserChatId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    private boolean isOwner;

    private ZonedDateTime joinedAt;

    public UserChatEntity(UserEntity user, ChatEntity chat) {
        this.user = user;
        this.chat = chat;
        this.isOwner = false;
        this.joinedAt = ZonedDateTime.now();
        this.id = new UserChatId(user.getId(), chat.getId());
    }


}
