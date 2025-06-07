package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.SendContactRequest;
import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.adapter.in.dto.responses.ContactResponse;
import com.br.chat.adapter.in.dto.responses.PendingContactResponse;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.port.in.contact.*;
import com.br.chat.core.port.in.message.NotificationUserPortIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactControllerAdapterIn {

    private final SendContactRequestPortIn sendContactRequestPortIn;
    private final ListUserContactsPortIn listUserContactsPortIn;
    private final ListUserPendingContactsPortIn listUserPendingContactsPortIn;
    private final UpdateContactRequestStatusPortIn updateContactRequestStatusPortIn;
    private final NotificationUserPortIn notificationUserPortIn;
    private final RemoveContactPortIn removeContactPortIn;

    @PostMapping
    public void sendContactRequest(@RequestBody @Valid SendContactRequest request, JwtAuthenticationToken token) {
        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        var notificationMessage = sendContactRequestPortIn.execute(UUID.fromString(userId), request.email());
        if (Objects.nonNull(notificationMessage)) {
            notificationUserPortIn.execute(notificationMessage, "/topics/notifications/%s".formatted(notificationMessage.getDestinationId()));
        }
    }

    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContact(@PathVariable Long contactId) {
        removeContactPortIn.execute(contactId);
    }

    @GetMapping
    public List<ContactResponse> findUserContacts(JwtAuthenticationToken token) {
        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        return listUserContactsPortIn.execute(UUID.fromString(userId));
    }

    @GetMapping("/invites")
    public List<PendingContactResponse> findPendingContactRequestS(JwtAuthenticationToken token) {
        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        return listUserPendingContactsPortIn.execute(UUID.fromString(userId));
    }

    @PatchMapping("/{contactId}/{status}")
    public void updateContactStatus(@PathVariable Long contactId, @PathVariable String status) {
        var updateContactRequest = new UpdateContactRequestStatus(contactId, ContactRequestStatus.valueOf(status));
        updateContactRequestStatusPortIn.execute(updateContactRequest);
    }
}