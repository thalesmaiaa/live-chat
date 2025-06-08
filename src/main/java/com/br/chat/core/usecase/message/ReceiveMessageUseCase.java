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
import com.br.chat.core.port.out.NotificationPortOut;
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
    private final NotificationPortOut notificationEventPublisher;

    @Override
    @Transactional
    public void execute(ChatMessage chatMessage) {
        var isStartingPrivateChat = chatMessage.getType().equals(ChatType.PRIVATE) && Objects.isNull(chatMessage.getId());

        if (isStartingPrivateChat) {
            startPrivateChat(chatMessage);
            notificationEventPublisher.publishEvent(generateChatNotifications(chatMessage));
            return;
        }

        updateChatMessages(chatMessage);
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

    private void updateChatMessages(ChatMessage chatMessage) {
        var senderId = chatMessage.getSenderUser().getId();
        var chat = chatRepositoryPortOut.findChatById(chatMessage.getId()).orElseThrow(ChatNotFoundException::new);
        var senderUser = chat.getUsers().stream().filter(user -> user.matchesId(senderId)).findFirst().orElse(null);
        if (Objects.isNull(senderUser)) return;

        chatMessage.setSenderUser(senderUser);

        messageRepositoryPortOut.saveChatMessage(chatMessage.getMessage(), chat);

        var usersNotifications = generateUsesNotifications(senderUser, chat);
        var chatNotifications = generateChatNotifications(chatMessage);

        usersNotifications.add(chatNotifications);
        notificationEventPublisher.publishAllEvents(usersNotifications);
    }

    private List<NotificationMessage> generateUsesNotifications(User sender, Chat chat) {
        var notificationType = NotificationType.NEW_MESSAGE;
        var chatMembers = chat.getUsers().stream().filter(user -> !user.matchesId(sender.getId())).toList();
        var senderUser = new User(sender.getId(), sender.getEmail(), sender.getUsername());
        var notifications = new ArrayList<NotificationMessage>();
        chatMembers.forEach(chatMember -> {
            var notificationMessage = new NotificationMessage();

            notificationMessage.setSenderUser(senderUser);
            notificationMessage.setDestinationId(chat.getId());
            notificationMessage.setSentAt(ZonedDateTime.now());
            notificationMessage.setNotificationType(notificationType);
            notificationMessage.setDestination(notificationType.getNotificationTopic().formatted(chatMember.getId()));
            notificationMessage.setMessage(notificationType.getMessage().formatted(sender.getUsername(), chat.getName()));

            notifications.add(notificationMessage);
        });

        return notifications;
    }

    private NotificationMessage generateChatNotifications(ChatMessage chatMessage) {
        var notificationMessage = chatMessage.toNotificationMessage();
        notificationMessage.setDestination("/topics/%s".formatted(notificationMessage.getDestinationId()));
        notificationMessage.setNotificationType(NotificationType.NEW_MESSAGE);
        return notificationMessage;
    }
}