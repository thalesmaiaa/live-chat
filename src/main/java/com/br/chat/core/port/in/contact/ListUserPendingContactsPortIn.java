package com.br.chat.core.port.in.contact;

import com.br.chat.adapter.in.dto.responses.PendingContactResponse;

import java.util.List;
import java.util.UUID;

public interface ListUserPendingContactsPortIn {

    List<PendingContactResponse> execute(UUID userId);
}
