package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.out.events.NotificationEventPublisher;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SendContactRequestUseCaseTest {

    private UserRepositoryPortOut userRepositoryPortOut;
    private ContactRepositoryPortOut contactRepositoryPortOut;
    private NotificationEventPublisher notificationEventPublisher;
    private SendContactRequestUseCase sendContactRequestUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        contactRepositoryPortOut = mock(ContactRepositoryPortOut.class);
        notificationEventPublisher = mock(NotificationEventPublisher.class);
        sendContactRequestUseCase = new SendContactRequestUseCase(userRepositoryPortOut, contactRepositoryPortOut, notificationEventPublisher);
    }

    @Test
    void shouldNotSendRequestToSelf() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "email", "username");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(user));
        when(userRepositoryPortOut.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        sendContactRequestUseCase.execute(userId, user.getEmail());
        verify(contactRepositoryPortOut, never()).save(any());
    }

    @Test
    void shouldSendContactRequestIfNotExists() {
        UUID userId = UUID.randomUUID();
        UUID requestedId = UUID.randomUUID();
        User requester = new User(userId, "email", "username");
        User requested = new User(requestedId, "other@email.com", "other");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(requester));
        when(userRepositoryPortOut.findByEmail(requested.getEmail())).thenReturn(Optional.of(requested));
        when(contactRepositoryPortOut.existsActiveRequestsByRequesterIdAndRequestedId(userId, requestedId)).thenReturn(false);

        sendContactRequestUseCase.execute(userId, requested.getEmail());
        verify(contactRepositoryPortOut).save(any(Contact.class));
        verify(notificationEventPublisher).publishEvent(any());
    }
}
