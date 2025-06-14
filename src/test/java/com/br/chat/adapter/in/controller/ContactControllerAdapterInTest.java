package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.SendContactRequest;
import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.adapter.in.dto.responses.ContactResponse;
import com.br.chat.adapter.in.dto.responses.PendingContactResponse;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.port.in.contact.*;
import com.br.chat.core.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerAdapterInTest {

    @Mock
    private SendContactRequestPortIn sendContactRequestPortIn;
    @Mock
    private ListUserContactsPortIn listUserContactsPortIn;
    @Mock
    private ListUserPendingContactsPortIn listUserPendingContactsPortIn;
    @Mock
    private UpdateContactRequestStatusPortIn updateContactRequestStatusPortIn;
    @Mock
    private RemoveContactPortIn removeContactPortIn;

    @InjectMocks
    private ContactControllerAdapterIn controller;

    @Test
    void shouldSendContactRequestCallPort() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var request = new SendContactRequest("test@email.com");
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            controller.sendContactRequest(request, token);
            verify(sendContactRequestPortIn).execute(userId, request.email());
        }
    }

    @Test
    void shouldRemoveContactCallPort() {
        var contactId = 123L;
        controller.removeContact(contactId);
        verify(removeContactPortIn).execute(contactId);
    }

    @Test
    void shouldFindUserContactsReturnContacts() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var contacts = List.of(mock(ContactResponse.class));
        when(listUserContactsPortIn.execute(userId)).thenReturn(contacts);
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            var result = controller.findUserContacts(token);
            assertThat(result).isEqualTo(contacts);
        }
    }

    @Test
    void shouldFindPendingContactRequestsReturnPendingContacts() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var pending = List.of(mock(PendingContactResponse.class));
        when(listUserPendingContactsPortIn.execute(userId)).thenReturn(pending);
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            var result = controller.findPendingContactRequestS(token);
            assertThat(result).isEqualTo(pending);
        }
    }

    @Test
    void shouldUpdateContactStatusCallPort() {
        var contactId = 1L;
        var status = ContactRequestStatus.ACCEPTED.name();
        controller.updateContactStatus(contactId, status);
        ArgumentCaptor<UpdateContactRequestStatus> captor = ArgumentCaptor.forClass(UpdateContactRequestStatus.class);
        verify(updateContactRequestStatusPortIn).execute(captor.capture());
        assertThat(captor.getValue().contactId()).isEqualTo(contactId);
        assertThat(captor.getValue().status()).isEqualTo(ContactRequestStatus.valueOf(status));
    }
}
