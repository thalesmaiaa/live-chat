package com.br.chat.adapter.out.persistence.user;

import com.br.chat.adapter.out.persistence.message.MessageEntity;
import com.br.chat.adapter.out.persistence.userchat.UserChatEntity;
import com.br.chat.core.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String email;
    private String password;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<UserChatEntity> userChats;

    @OneToMany(mappedBy = "user")
    private List<MessageEntity> messages;

    public static UserEntity fromDomain(User user) {
        var entity = new UserEntity();

        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setUpdatedAt(ZonedDateTime.now());
        return entity;
    }

    public User toDomain() {
        return new User(this.id, this.username, this.email, this.password, this.createdAt, this.updatedAt);
    }
}
