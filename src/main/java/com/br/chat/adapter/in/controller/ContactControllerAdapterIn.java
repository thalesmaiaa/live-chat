package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.SendContactRequest;
import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.adapter.in.dto.responses.ContactResponse;
import com.br.chat.adapter.in.dto.responses.PendingContactResponse;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.port.in.contact.*;
import com.br.chat.core.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactControllerAdapterIn {

    private final SendContactRequestPortIn sendContactRequestPortIn;
    private final ListUserContactsPortIn listUserContactsPortIn;
    private final ListUserPendingContactsPortIn listUserPendingContactsPortIn;
    private final UpdateContactRequestStatusPortIn updateContactRequestStatusPortIn;
    private final RemoveContactPortIn removeContactPortIn;

    @PostMapping
    public void sendContactRequest(@RequestBody @Valid SendContactRequest request, JwtAuthenticationToken token) {
        sendContactRequestPortIn.execute(JwtUtils.extractUserIdFromToken(token), request.email());
    }

    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContact(@PathVariable Long contactId) {
        removeContactPortIn.execute(contactId);
    }

    @GetMapping
    public List<ContactResponse> findUserContacts(JwtAuthenticationToken token) {
        return listUserContactsPortIn.execute(JwtUtils.extractUserIdFromToken(token));
    }

    @GetMapping("/invites")
    public List<PendingContactResponse> findPendingContactRequestS(JwtAuthenticationToken token) {
        return listUserPendingContactsPortIn.execute(JwtUtils.extractUserIdFromToken(token));
    }

    @PatchMapping("/{contactId}/{status}")
    public void updateContactStatus(@PathVariable Long contactId, @PathVariable String status) {
        var updateContactRequest = new UpdateContactRequestStatus(contactId, ContactRequestStatus.valueOf(status));
        updateContactRequestStatusPortIn.execute(updateContactRequest);
    }
}