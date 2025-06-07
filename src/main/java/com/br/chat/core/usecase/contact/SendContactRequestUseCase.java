package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.domain.message.NotificationMessage;
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

    @Override
    public NotificationMessage execute(UUID userId, String email) {
        var requesterUser = userRepositoryPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        var requestedUser = userRepositoryPortOut.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (userRequestedMatches(userId, requestedUser.getId())) return null;

        var existsActiveRequests = contactRepositoryPortOut.existsActiveRequestsByRequesterIdAndRequestedId(
                requesterUser.getId(), requestedUser.getId());

        if (!existsActiveRequests) {
            var contactRequest = new Contact(requesterUser, requestedUser, ContactRequestStatus.PENDING, ZonedDateTime.now());
            contactRepositoryPortOut.save(contactRequest);
            return generateNotificationMessage(contactRequest);
        }

        return null;
    }

    private NotificationMessage generateNotificationMessage(Contact contact) {
        var notificationMessage = new NotificationMessage();
        notificationMessage.setMessage("You have a new contact request from %s".formatted(contact.getRequesterUser().getUsername()));
        notificationMessage.setDestinationId(contact.getRequestedUser().getId());
        notificationMessage.setSentAt(ZonedDateTime.now());
        notificationMessage.setSenderUser(new NotificationMessage.SenderUser(
                contact.getRequesterUser().getId(),
                contact.getRequesterUser().getUsername(),
                contact.getRequesterUser().getEmail()));

        return notificationMessage;
    }


    private boolean userRequestedMatches(UUID requesterId, UUID requestedId) {
        return requesterId.equals(requestedId);
    }
}