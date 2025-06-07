package com.br.chat.core.domain.chat;

import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    private UUID id;
    private UUID receiverId;
    private ChatType type;
    private ZonedDateTime createdAt;
    private Message message;
    private User senderUser;

    public ChatMessage(UUID id, UUID senderId, UUID receiverId, ChatType chatType, String message, ZonedDateTime sentAt) {
        this.id = id;
        this.senderUser = new User();
        this.senderUser.setId(senderId);
        this.receiverId = receiverId;
        this.type = chatType;
        this.createdAt = ZonedDateTime.now();
        this.message = new Message(this.senderUser, message, sentAt);
    }

    public Chat toChat() {
        var chat = new Chat();
        chat.setId(this.id);
        chat.setType(this.type);
        chat.setCreatedAt(this.createdAt);
        chat.addMessage(this.message);
        return chat;
    }

    public NotificationMessage toNotificationMessage() {
        var notificationMessage = new NotificationMessage();
        notificationMessage.setDestinationId(this.receiverId);
        notificationMessage.setMessage(this.message.getContent());
        notificationMessage.setSentAt(this.createdAt);
        notificationMessage.setSenderUser(new User(this.senderUser.getId(), this.senderUser.getUsername(),
                this.senderUser.getEmail()));
        return notificationMessage;
    }
}