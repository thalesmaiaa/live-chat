package com.br.chat.adapter.in.dto.requests;

import com.br.chat.core.domain.contact.ContactRequestStatus;

public record UpdateContactRequestStatus(Long contactId, ContactRequestStatus status) {
}