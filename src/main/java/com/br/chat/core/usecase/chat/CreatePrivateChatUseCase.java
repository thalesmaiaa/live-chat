package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.Message;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.domain.message.NotificationType;
import com.br.chat.core.events.NotificationEventPublisher;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.chat.CreatePrivateChatPortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreatePrivateChatUseCase implements CreatePrivateChatPortIn {

    private final ChatRepositoryPortOut chatRepositoryPortOut;
    private final UserRepositoryPortOut userRepositoryPortOut;
    private final NotificationEventPublisher publisher;

    @Override
    public UUID execute(CreatePrivateChatRequest request) {
        var sender = userRepositoryPortOut.findById(request.getSenderId()).orElseThrow(UserNotFoundException::new);
        var receiver = userRepositoryPortOut.findById(request.getReceiverId()).orElseThrow(UserNotFoundException::new);
        var chat = new Chat();
        chat.setType(ChatType.PRIVATE);
        chat.setName(sender.getUsername() + " & " + receiver.getUsername());
        chat.setMessages(List.of(new Message(sender, request.getMessage(), ZonedDateTime.now())));
        chat.setUsers(List.of(sender, receiver));

        var chatId = chatRepositoryPortOut.startChat(chat);

        var notificationMessage = new NotificationMessage();
        var notificationType = NotificationType.UNREAD_MESSAGES;

        notificationMessage.setSenderUser(sender);
        notificationMessage.setDestinationId(chatId);
        notificationMessage.setSentAt(ZonedDateTime.now());
        notificationMessage.setNotificationType(notificationType);
        notificationMessage.setMessage(notificationType.getMessage().formatted(sender.getUsername()));
        notificationMessage.setDestination(notificationType.getNotificationTopic().formatted(receiver.getId()));

        publisher.publishEvent(notificationMessage);

        return chatId;
    }
}
