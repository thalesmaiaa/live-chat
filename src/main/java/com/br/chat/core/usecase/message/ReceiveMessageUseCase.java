package com.br.chat.core.usecase.message;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatMessage;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.domain.message.NotificationType;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.ChatNotFoundException;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.message.ReceiveMessagePortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.MessageRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReceiveMessageUseCase implements ReceiveMessagePortIn {

    private final ChatRepositoryPortOut chatRepositoryPortOut;
    private final UserRepositoryPortOut userRepositoryPortOut;
    private final MessageRepositoryPortOut messageRepositoryPortOut;


    @Override
    @Transactional
    public List<NotificationMessage> execute(ChatMessage chatMessage) {
        var isStartingPrivateChat = chatMessage.getType().equals(ChatType.PRIVATE) && Objects.isNull(chatMessage.getId());

        if (isStartingPrivateChat) {
            startPrivateChat(chatMessage);
            return generateChatNotificationMessage(chatMessage);
        }

        var senderId = chatMessage.getSenderUser().getId();
        var chat = chatRepositoryPortOut.findChatById(chatMessage.getId()).orElseThrow(ChatNotFoundException::new);
        var senderUser = chat.getUsers().stream().filter(user -> user.getId().equals(senderId)).findFirst();
        if (senderUser.isEmpty()) return List.of();

        chatMessage.setSenderUser(senderUser.orElse(null));
        messageRepositoryPortOut.saveChatMessage(chatMessage.getMessage(), chat);
        var chatNotifications = generateChatNotificationMessage(chatMessage);
        var usersNotifications = generateUsersNotificationMessage(senderUser.orElse(null), chat);
        usersNotifications.addAll(chatNotifications);
        return usersNotifications;
    }

    private void startPrivateChat(ChatMessage chatMessage) {
        var sender = chatMessage.getSenderUser();
        var senderUser = userRepositoryPortOut.findById(sender.getId())
                .orElseThrow(UserNotFoundException::new);
        var receiverUser = userRepositoryPortOut.findById(chatMessage.getReceiverId())
                .orElseThrow(UserNotFoundException::new);

        var chat = chatMessage.toChat();
        chat.setUsers(List.of(senderUser, receiverUser));
        chatRepositoryPortOut.startChat(chat);
        chatMessage.setSenderUser(senderUser);
    }

    private List<NotificationMessage> generateUsersNotificationMessage(User sender, Chat chat) {
        var chatMembers = chat.getUsers().stream().filter(user -> !user.getId().equals(sender.getId())).toList();
        var notifications = new ArrayList<NotificationMessage>();
        chatMembers.forEach(chatMember -> {
            var notificationMessage = new NotificationMessage();
            notificationMessage.setSentAt(ZonedDateTime.now());
            notificationMessage.setDestinationId(chat.getId());
            notificationMessage.setNotificationType(NotificationType.NEW_MESSAGE);
            notificationMessage.setDestination("/topics/notifications/%s".formatted(chatMember.getId()));
            notificationMessage.setMessage(NotificationType.NEW_MESSAGE.getMessage().formatted(sender.getUsername(), chat.getName()));
            notificationMessage.setSenderUser(new NotificationMessage.SenderUser(sender.getId(), sender.getEmail(), sender.getUsername()));
            notifications.add(notificationMessage);
        });
        return notifications;
    }

    private List<NotificationMessage> generateChatNotificationMessage(ChatMessage chatMessage) {
        var notificationMessage = chatMessage.toNotificationMessage();
        notificationMessage.setDestination("/topics/%s".formatted(notificationMessage.getDestinationId()));
        notificationMessage.setNotificationType(NotificationType.NEW_MESSAGE);
        return List.of(notificationMessage);
    }
}