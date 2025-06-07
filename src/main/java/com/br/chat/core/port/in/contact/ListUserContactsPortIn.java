package com.br.chat.core.port.in.contact;

import com.br.chat.adapter.in.dto.responses.ContactResponse;

import java.util.List;
import java.util.UUID;

public interface ListUserContactsPortIn {

    List<ContactResponse> execute(UUID userId);


}
