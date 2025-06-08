package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import com.br.chat.core.port.out.NotificationPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendContactRequestUseCaseTest {

    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;
    @Mock
    private ContactRepositoryPortOut contactRepositoryPortOut;
    @Mock
    private NotificationPortOut notificationEventPublisher;

    @InjectMocks
    private SendContactRequestUseCase sendContactRequestUseCase;

    @Test
    void shouldNotSendRequestToSelf() {
        var userId = UUID.randomUUID();
        var user = new User(userId, "email", "username");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(user));
        when(userRepositoryPortOut.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        sendContactRequestUseCase.execute(userId, user.getEmail());
        verify(contactRepositoryPortOut, never()).save(any());
    }

    @Test
    void shouldSendContactRequestIfNotExists() {
        var userId = UUID.randomUUID();
        var requestedId = UUID.randomUUID();
        var requester = new User(userId, "email", "username");
        var requested = new User(requestedId, "other@email.com", "other");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(requester));
        when(userRepositoryPortOut.findByEmail(requested.getEmail())).thenReturn(Optional.of(requested));
        when(contactRepositoryPortOut.existsActiveRequestsByRequesterIdAndRequestedId(userId, requestedId)).thenReturn(false);

        sendContactRequestUseCase.execute(userId, requested.getEmail());
        verify(contactRepositoryPortOut).save(any(Contact.class));
        verify(notificationEventPublisher).publishEvent(any());
    }
}
