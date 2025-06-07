package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.core.exception.ContactRequestNotFoundException;
import com.br.chat.core.port.in.contact.UpdateContactRequestStatusPortIn;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateContactRequestUseCase implements UpdateContactRequestStatusPortIn {

    private final ContactRepositoryPortOut contactRepositoryPortOut;

    @Override
    public void execute(UpdateContactRequestStatus request) {
        var contact = contactRepositoryPortOut.findById(request.contactId()).orElseThrow(ContactRequestNotFoundException::new);
        contact.setStatus(request.status());
        contactRepositoryPortOut.save(contact);
    }
}
