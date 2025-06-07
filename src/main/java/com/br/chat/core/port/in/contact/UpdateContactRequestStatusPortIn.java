package com.br.chat.core.port.in.contact;

import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;

public interface UpdateContactRequestStatusPortIn {

    void execute(UpdateContactRequestStatus request);
}
