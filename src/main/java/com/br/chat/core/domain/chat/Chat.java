package com.br.chat.core.domain.chat;

import com.br.chat.adapter.in.dto.responses.ChatResponse;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Chat {

    private UUID id;
    private ChatType type;
    private UUID ownerId;
    private String name;
    private ZonedDateTime createdAt;
    private List<Message> messages = new ArrayList<>();
    private List<User> users;

    public Chat(String name, UUID ownerId, ChatType type, List<User> users, ZonedDateTime createdAt) {
        this.ownerId = ownerId;
        this.type = type;
        this.createdAt = createdAt;
        this.users = users;
        this.name = name;
    }

    public static Chat toGroupChat(String name, UUID ownerId, List<User> members) {
        return new Chat(name, ownerId, ChatType.GROUP, members, ZonedDateTime.now());
    }

    public Boolean isPrivateChat() {
        return this.type == ChatType.PRIVATE;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public ChatResponse toChatResponse() {
        return new ChatResponse(
                this.ownerId,
                this.id,
                this.name,
                this.type,
                this.users.stream().map(User::toUserResponse).toList(),
                this.messages.stream().map(Message::toMessageResponse).toList(),
                this.createdAt);
    }
}