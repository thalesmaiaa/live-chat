package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.domain.message.NotificationMessage;
import com.br.chat.core.domain.message.NotificationType;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.events.NotificationEventPublisher;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.contact.SendContactRequestPortIn;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SendContactRequestUseCase implements SendContactRequestPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final ContactRepositoryPortOut contactRepositoryPortOut;
    private final NotificationEventPublisher notificationEventPublisher;

    @Override
    public void execute(UUID userId, String email) {
        var requesterUser = userRepositoryPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        var requestedUser = userRepositoryPortOut.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (userRequestedMatches(userId, requestedUser.getId())) return;

        var existsActiveRequests = contactRepositoryPortOut.existsActiveRequestsByRequesterIdAndRequestedId(
                requesterUser.getId(), requestedUser.getId());

        if (!existsActiveRequests) {
            var contactRequest = new Contact(requesterUser, requestedUser, ContactRequestStatus.PENDING, ZonedDateTime.now());
            contactRepositoryPortOut.save(contactRequest);
            var notificationMessage = generateNotificationMessage(contactRequest);
            notificationEventPublisher.publishEvent(notificationMessage);
        }

    }

    private NotificationMessage generateNotificationMessage(Contact contact) {
        var notificationMessage = new NotificationMessage();
        var notificationType = NotificationType.CONTACT_REQUEST;
        var contactSender = contact.getRequesterUser();
        var senderUser = new User(contactSender.getId(), contactSender.getUsername(), contactSender.getEmail());

        notificationMessage.setMessage(notificationType.getMessage().formatted(contactSender.getUsername()));
        notificationMessage.setDestinationId(contact.getRequestedUser().getId());
        notificationMessage.setDestination(notificationType.getNotificationTopic().formatted(notificationMessage.getDestinationId()));
        notificationMessage.setSentAt(ZonedDateTime.now());
        notificationMessage.setNotificationType(notificationType);
        notificationMessage.setSenderUser(senderUser);

        return notificationMessage;
    }

    private boolean userRequestedMatches(UUID requesterId, UUID requestedId) {
        return requesterId.equals(requestedId);
    }
}